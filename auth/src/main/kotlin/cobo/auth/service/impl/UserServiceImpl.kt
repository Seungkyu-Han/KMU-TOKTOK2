package cobo.auth.service.impl

import cobo.auth.config.response.CoBoResponse
import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.user.GetUserListRes
import cobo.auth.data.dto.user.PutUserReq
import cobo.auth.data.dto.user.UserRes
import cobo.auth.repository.UserRepository
import cobo.auth.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService{
    override fun getList(page: Int, pageSize: Int): ResponseEntity<CoBoResponseDto<GetUserListRes>> {
        val pageUser = userRepository.findAll(PageRequest.of(page, pageSize))
        return CoBoResponse(
            GetUserListRes(
                users = pageUser.toList().map{
                    UserRes(it)
                },
                totalElements = pageUser.totalElements
            ), CoBoResponseStatus.SUCCESS).getResponseEntityWithLog()
    }

    override fun put(putUserReq: PutUserReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return CoBoResponse<CoBoResponseStatus>(
            if (userRepository.updateUserByStudentIdWithJDBC(
                    studentId = putUserReq.studentId,
                    role = putUserReq.roleEnum,
                    registerState = putUserReq.registerStateEnum) > 0)
                CoBoResponseStatus.SUCCESS
            else
                CoBoResponseStatus.NO_DATA_CHANGES).getResponseEntityWithLog()
    }
}