package com.clangengineer.surveymodus.web.rest.vm

class StaffLoginVM(
//    @field:NotNull
//    @field:Pattern(regexp = "^[0-9]{9,10}\$")
    var phone: String? = null,

//    @field:NotNull
//    @field:Size(min = 6, max = 6)
    var otp: String? = null
) {
    override fun toString() = "StaffLoginVM{" +
        "phone='" + phone + '\''.toString() +
        ", otp='" + otp + '\''.toString() +
        '}'.toString()
}
