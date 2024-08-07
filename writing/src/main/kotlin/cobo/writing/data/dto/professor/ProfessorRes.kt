package cobo.writing.data.dto.professor

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class AssignmentGetListElementRes(
    @Schema(description = "과제 ID", example = "1")
    val id: Int,
    @Schema(description = "과제 제목", example = "1주차 과제입니다.")
    val title: String,
    @Schema(description = "과제 설명", example = "인공지능에 대하여 글 작성하기")
    val description: String,
    @Schema(description = "과제 점수", example = "10")
    var score: Int,
    @Schema(description = "시작일", example = "2024-08-05")
    var startDate: LocalDate,
    @Schema(description = "마감일", example = "2024-08-06")
    var endDate: LocalDate
)

data class AssignmentGetListRes(
    val assignments: List<AssignmentGetListElementRes>
)