package ws.delegate

import play.api.data._
import play.api.data.Forms._
import ws.services.PatientList

object PatientDelegate {

  val _patientProfileForm = Form(
    mapping(
      "id" -> text,
      "firstName" -> text,
      "middleName" -> text,
      "lastName" -> text,
      "medicalHistory" -> text,
      "address" -> text,
      "contactNo" -> text,
      "dateOfBirth" -> text,
      "image" -> text
    )(PatientList.apply)(PatientList.unapply)
  )

}

