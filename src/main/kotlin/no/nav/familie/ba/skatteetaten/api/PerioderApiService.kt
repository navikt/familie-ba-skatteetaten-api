package org.openapitools.api

import org.openapitools.model.PerioderRequest
import org.openapitools.model.PerioderResponse
interface PerioderApiService {

	fun hentPerioderMedUtvidetBarnetrygd(aar: kotlin.String, authorization: kotlin.String, perioderRequest: PerioderRequest): PerioderResponse
}
