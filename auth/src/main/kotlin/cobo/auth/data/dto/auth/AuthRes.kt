package cobo.auth.data.dto.auth

import cobo.auth.data.enums.RegisterStateEnum
import io.swagger.v3.oas.annotations.media.Schema

data class GetAuthLoginRes(

    @Schema(description = "크무톡톡 자체 AccessToken")
    val  accessToken: String,

    @Schema(description = "크무톡톡 자체 RefreshToken")
    val refreshToken: String,

    @Schema(description = "회원가입 상태", example = "ACTIVE")
    val registerStateEnum: RegisterStateEnum,

    @Schema(description = "학번")
    val studentId: String?
)
