package cobo.auth.service.oauth.impl

import cobo.auth.data.entity.Oauth
import cobo.auth.repository.OauthRepository
import cobo.auth.service.oauth.OauthService
import org.springframework.stereotype.Service

@Service
class KakaoOauthServiceImpl(
    private val oauthRepository: OauthRepository
) : OauthService {
    override fun getOauth(accessToken: String): Oauth {
        TODO("Not yet implemented")
    }

    override fun getAccessToken(code: String, redirectUri: String): String {

        return ""
    }
}