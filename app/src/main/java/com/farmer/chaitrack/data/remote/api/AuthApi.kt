package com.farmer.chaitrack.data.remote.api

import com.farmer.chaitrack.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<LogoutResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    @GET("auth/verify")
    suspend fun verifyToken(
        @Header("Authorization") token: String
    ): Response<VerifyTokenResponse>

    @POST("auth/change-pin")
    suspend fun changePin(
        @Header("Authorization") token: String,
        @Body request: ChangePinRequest
    ): Response<ChangePinResponse>

    @POST("auth/forgot-pin")
    suspend fun forgotPin(
        @Body request: ForgotPinRequest
    ): Response<ForgotPinResponse>

    @POST("auth/verify-recovery-code")
    suspend fun verifyRecoveryCode(
        @Body request: VerifyRecoveryCodeRequest
    ): Response<VerifyRecoveryCodeResponse>

    @POST("auth/reset-pin")
    suspend fun resetPin(
        @Body request: ResetPinRequest
    ): Response<ResetPinResponse>

    /**@POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>
    */

    @GET("auth/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserProfileResponse>

    @PUT("auth/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<UpdateProfileResponse>

    @Multipart
    @POST("auth/profile/image")
    suspend fun uploadProfileImage(
        @Header("Authorization") token: String,
        @Part("image") image: okhttp3.MultipartBody.Part
    ): Response<UploadImageResponse>

    /** @DELETE("auth/account")
    suspend fun deleteAccount(
        @Header("Authorization") token: String,
        @Body request: DeleteAccountRequest
    ): Response<DeleteAccountResponse>
    */

    @POST("auth/check-phone")
    suspend fun checkPhoneNumber(
        @Body request: CheckPhoneRequest
    ): Response<CheckPhoneResponse>

    @POST("auth/request-otp")
    suspend fun requestOtp(
        @Body request: RequestOtpRequest
    ): Response<RequestOtpResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<VerifyOtpResponse>
}