package no.nav.familie.ba.skatteetaten.config

import no.nav.familie.log.filter.LogFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SecurityHeaderFilter : HttpFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilter(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {

        httpServletResponse.setHeader("Cache-Control", "must-revalidate,no-cache,no-store")
        httpServletResponse.setHeader("X-Content-Type-Options", "nosniff")
        if (httpServletResponse.getHeader("Set-Cookie") != null) {
            httpServletResponse.setHeader("Set-Cookie", "${httpServletResponse.getHeader("Set-Cookie")};SameSite=strict")
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse)
    }

    private fun resolveUserId(httpServletRequest: HttpServletRequest): Cookie {
        return httpServletRequest.cookies?.firstOrNull { it -> "RUIDC" == it.name }!!
    }

    override fun init(filterConfig: FilterConfig) {}

    override fun destroy() {}

    companion object

}
