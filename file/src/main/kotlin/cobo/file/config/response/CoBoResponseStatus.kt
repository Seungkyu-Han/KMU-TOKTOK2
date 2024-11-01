package cobo.file.config.response

enum class CoBoResponseStatus(
    val code: Int,
    val message: String
) {

    SUCCESS(2000, "OK"),
    NO_DATA_CHANGES(2001, "SUCCESS, BUT NO DATA CHANGES"),
    CREATED(2010, "CREATED_SUCCESS"),

    BAD_REQUEST(4000, "BAD REQUEST"),
    NOT_AUTHORIZATION(4011, "AUTHORIZATION TOKEN IS EMPTY"),
    NOT_FOUND_USER(4041, "USER NOT FOUND"),
    CANT_SAVE(4042, "CANT SAVE IN DATABASE"),
    NOT_FOUND_STUDENT(4043, "TARGET STUDENT NOT FOUND"),
    CANT_GET_RESOURCES(4044, "CANT GET RESOURCES"),
    NOT_FOUND_FILE(4045, "FILE NOT FOUND"),
    NOT_FOUND_CATEGORY(4046, "CATEGORY NOT_FOUND"),

    EXIST_DATA(4091, "CONFLICT DATA")
}