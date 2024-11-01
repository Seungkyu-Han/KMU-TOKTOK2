package cobo.auth.service.impl

import cobo.auth.config.LogFilter
import cobo.auth.config.jwt.JwtTokenProvider
import cobo.auth.config.response.CoBoResponse
import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.auth.GetAuthLoginRes
import cobo.auth.data.dto.auth.PostAuthRegisterReq
import cobo.auth.data.entity.User
import cobo.auth.data.enums.OauthTypeEnum
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.OauthRepository
import cobo.auth.repository.StudentInfoRepository
import cobo.auth.repository.UserRepository
import cobo.auth.service.AuthService
import cobo.auth.service.oauth.impl.KakaoOauthServiceImpl
import cobo.auth.service.oauth.impl.NaverOauthServiceImpl
import jakarta.security.auth.message.AuthException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class AuthServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val oauthRepository: OauthRepository,
    private val studentInfoRepository: StudentInfoRepository,
    private val kakaoOauthServiceImpl: KakaoOauthServiceImpl,
    private val naverOauthServiceImpl: NaverOauthServiceImpl,
    @Value("\${kakao.auth.redirect_uri}")
    private val kakaoRedirectUri: String,
    @Value("\${kakao.auth.local_redirect_uri}")
    private val kakaoLocalRedirectUri: String,
    @Value("\${kakao.auth.admin_redirect_uri}")
    private val kakaoAdminRedirectUri: String,
) : AuthService {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LogFilter::class.java)
    }

    override fun getKakaoLogin(
        code: String
    ): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>> {
        return this.login(OauthTypeEnum.KAKAO, code, kakaoRedirectUri, false)
    }

    override fun getNaverLogin(code: String): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>> {
        return this.login(OauthTypeEnum.NAVER, code, "", false)
    }

    override fun postRegister(
        postAuthRegisterReq: PostAuthRegisterReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>> {

        val userId = authentication.name.toInt()

        if (userRepository.findById(userId).orElseThrow{NullPointerException()}.registerState == RegisterStateEnum.ACTIVE)
            throw IllegalAccessException("ALREADY_REGISTERED")

        if (!studentInfoRepository.existsByStudentIdAndName(postAuthRegisterReq.studentId, postAuthRegisterReq.name))
            throw NullPointerException()

        val user = userRepository.findByStudentIdWithJDBC(postAuthRegisterReq.studentId)

        val tokenList: Array<String>

        if(user.isPresent){
            oauthRepository.updateUserIdByUserIdWithJDBC(
                oldUserId = userId,
                newUserId = user.get().id ?: userId)
            userRepository.deleteById(userId)
            tokenList = getAccessTokenAndRefreshTokenByUser(user.get())
        }
        else{
            userRepository.updateStudentIdWithJDBC(
                id = userId,
                studentId = postAuthRegisterReq.studentId,
                registerStateEnum = RegisterStateEnum.ACTIVE
            )
            tokenList = getAccessTokenAndRefreshTokenByUser(User(userId, postAuthRegisterReq.studentId))
        }

        val coBoResponse = CoBoResponse(GetAuthLoginRes(tokenList[0], tokenList[1], RegisterStateEnum.ACTIVE, postAuthRegisterReq.studentId), CoBoResponseStatus.SUCCESS)

        return coBoResponse.getResponseEntityWithLog()
    }

    override fun patchLogin(authorization: String?): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>> {

        val token = authorization?.split(" ")?.get(1)!!

        val userId = jwtTokenProvider.getId(token).toInt()
        val studentId = jwtTokenProvider.getStudentId(token)
        val roleEnum = jwtTokenProvider.getRole(token)

        return CoBoResponse(
            GetAuthLoginRes(
                accessToken = jwtTokenProvider.getAccessToken(userId, studentId, roleEnum ?: RoleEnum.STUDENT),
                refreshToken = token,
                registerStateEnum = userRepository.findById(userId).orElseThrow{NullPointerException()}.registerState,
                studentId = studentId),
            CoBoResponseStatus.SUCCESS).getResponseEntityWithLog()
    }

    override fun getLocalKakaoLogin(code: String): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>> {
        return this.login(OauthTypeEnum.KAKAO, code, kakaoLocalRedirectUri, false)
    }

    override fun getAdminKakaoLogin(code: String): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>> {
        return this.login(OauthTypeEnum.KAKAO, code, kakaoAdminRedirectUri, true)
    }

    private fun getUserByOauthCode(code: String, oauthTypeEnum: OauthTypeEnum, redirectUri: String): User {
        val oauth = when(oauthTypeEnum) {
            OauthTypeEnum.KAKAO -> kakaoOauthServiceImpl.getOauth(code, redirectUri)
            OauthTypeEnum.NAVER -> naverOauthServiceImpl.getOauth(code, redirectUri)
        }

        if (oauth.user != null) {
            return oauth.user ?: throw NullPointerException()
        } else{
            val user = userRepository.save(
                User(
                    id = null,
                    studentId = null,
                    role = RoleEnum.STUDENT,
                    registerState = RegisterStateEnum.INACTIVE
                )
            )
            CompletableFuture.runAsync {
                oauth.user = user
                oauthRepository.save(oauth)
            }.exceptionally {
                logger.error("Failed to save user {} {}", code, oauthTypeEnum.name)
                null
            }
            return user
        }
    }

    private fun getAccessTokenAndRefreshTokenByUser(user: User): Array<String>{
        return arrayOf(
            jwtTokenProvider.getAccessToken(user.id ?: throw NullPointerException(), user.studentId, user.role),
            jwtTokenProvider.getRefreshToken(user.id ?: throw NullPointerException(), user.studentId, user.role)
        )
    }

    private fun login(oauthTypeEnum: OauthTypeEnum, code: String, redirectUri: String, isAdmin: Boolean): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>> {
        val user = getUserByOauthCode(code, oauthTypeEnum, redirectUri)

        if(isAdmin && user.role == RoleEnum.STUDENT)
            throw AuthException("NOT AUTHENTICATE")

        val tokenList = getAccessTokenAndRefreshTokenByUser(user)

        val coBoResponse = CoBoResponse(GetAuthLoginRes(tokenList[0], tokenList[1], user.registerState, user.studentId), CoBoResponseStatus.SUCCESS)

        return coBoResponse.getResponseEntityWithLog()
    }

    override fun getAdminNaverLogin(code: String): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>> {
        return this.login(OauthTypeEnum.NAVER, code, "", true)
    }

}