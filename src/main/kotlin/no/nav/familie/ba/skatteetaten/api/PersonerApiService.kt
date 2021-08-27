package org.openapitools.api

import org.openapitools.model.PersonerResponse
interface PersonerApiService {

	fun finnPersonerMedUtvidetBarnetrygd(aar: kotlin.String, authorization: kotlin.String): PersonerResponse
}
