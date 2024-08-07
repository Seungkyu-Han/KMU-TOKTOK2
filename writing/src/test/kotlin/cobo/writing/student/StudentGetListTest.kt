package cobo.writing.student

import cobo.writing.data.entity.Assignment
import cobo.writing.data.entity.Writing
import cobo.writing.data.enums.WritingStateEnum
import cobo.writing.repository.AssignmentRepository
import cobo.writing.repository.WritingRepository
import cobo.writing.service.AssignmentService
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentGetListTest @Autowired constructor(
    private val assignmentRepository: AssignmentRepository,
    private val writingRepository : WritingRepository,
    private val assignmentService: AssignmentService
) {

    private val studentId = "TEST_STUDENT_ID"

    private val assignmentList = mutableListOf<Assignment>()
    private val writingList = mutableListOf<Writing>()

    @AfterEach
    fun clear(){
        writingRepository.deleteAll(writingList)
        writingList.clear()

        assignmentRepository.deleteAll(assignmentList)
        assignmentList.clear()
    }

    private fun makeTestAssignment():Assignment{
        return Assignment(
            id = null,
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            score = (1..20).random(),
            startDate = LocalDate.of(2024, (1..7).random(), (1..20).random()),
            endDate = LocalDate.of(2024, (8..12).random(), (1..20).random()),
        )
    }

    private fun makeTestWriting(assignment: Assignment, state: WritingStateEnum):Writing {
        return Writing(
            id = null,
            studentId = studentId,
            assignment = assignment,
            content = UUID.randomUUID().toString(),
            state = state,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            submittedAt = LocalDateTime.now()
        )
    }


}