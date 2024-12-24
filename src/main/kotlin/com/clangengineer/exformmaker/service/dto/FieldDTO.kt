package com.clangengineer.exformmaker.service.dto

import com.clangengineer.exformmaker.domain.embeddable.FieldAttribute
import java.io.Serializable
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@SuppressWarnings("common-java:DuplicatedBlocks")
data class FieldDTO(
  var id: Long? = null,

  @get: NotNull
  @get: Size(min = 5, max = 100)
  var title: String? = null,

  var description: String? = null,

  var activated: Boolean? = null,

  var form: FormDTO? = null,

  var attribute: FieldAttribute? = null
) : Serializable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is FieldDTO) return false
    val fieldDTO = other
    if (this.id == null) {
      return false
    }
    return Objects.equals(this.id, fieldDTO.id)
  }

  override fun hashCode() = Objects.hash(this.id)
}
