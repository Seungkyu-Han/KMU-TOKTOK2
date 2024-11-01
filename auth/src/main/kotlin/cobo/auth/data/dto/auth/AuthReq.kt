package cobo.auth.data.dto.auth

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class PostAuthRegisterReq(
    @NotNull
    @Schema(description = "학번", example = "2021111222")
    val studentId: String,

    @NotNull
    @Schema(description = "이메일", example = "trust1204@stu.kmu.ac.kr")
    val name: String
)
