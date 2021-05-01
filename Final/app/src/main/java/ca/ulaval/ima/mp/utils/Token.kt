package ca.ulaval.ima.mp.utils

public class Token {

    private var instance: Token? = null

    private var authToken: String? = null
    private var refreshToken: String? = null
    private var clientId: String? = null
    private var clientSecret: String? = null

    private fun Token() {
        authToken = ""
        refreshToken = ""
        clientId = ""
        clientSecret = ""
    }

    fun getAuthToken(): String? {
        return authToken
    }

    fun getRefreshToken(): String? {
        return refreshToken
    }

    fun isLoggedIn(): Boolean? {
        return authToken!!.length > 0
    }

    fun setAuthToken(authToken: String?) {
        this.authToken = authToken
    }

    fun setRefreshToken(refreshToken: String?) {
        this.refreshToken = refreshToken
    }

    fun getClientId(): String? {
        return clientId
    }

    fun getClientSecret(): String? {
        return clientSecret
    }

    @Synchronized
    fun getInstance(): Token? {
        if (instance == null) {
            instance = ca.ulaval.ima.mp.utils.Token()
        }
        return instance
    }
}