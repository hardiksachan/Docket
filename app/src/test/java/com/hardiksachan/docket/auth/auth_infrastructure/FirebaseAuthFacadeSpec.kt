package com.hardiksachan.docket.auth.auth_infrastructure

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.hardiksachan.docket.auth.auth_domain.AuthFailure
import com.hardiksachan.docket.auth.auth_domain.EmailAddress
import com.hardiksachan.docket.auth.auth_domain.Password
import com.hardiksachan.docket.core.MainCoroutineListener
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.mockk.*
import strikt.api.expect
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight
import strikt.assertions.isEqualTo

class FirebaseAuthFacadeSpec : FunSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val coroutineListener = MainCoroutineListener()
    listeners(coroutineListener)

    val authMock = mockk<FirebaseAuth>()

    lateinit var firebaseAuthFacade: FirebaseAuthFacade

    beforeTest {
        firebaseAuthFacade = FirebaseAuthFacade(authMock)
    }

    afterTest {
        clearAllMocks()
    }

    context("registerWithEmailAndPassword is called") {
        val emailSlot = slot<String>()
        val passwordSlot = slot<String>()

        val authResultTaskMock: Task<AuthResult> = mockk()

        every {
            authMock.createUserWithEmailAndPassword(
                capture(emailSlot),
                capture(passwordSlot)
            )
        } returns authResultTaskMock

        val listenerSlot: CapturingSlot<OnCompleteListener<AuthResult>> = slot()

        every {
            authResultTaskMock
                .addOnCompleteListener(capture(listenerSlot))
        } answers {
            listenerSlot.captured.onComplete(authResultTaskMock)
            mockk()
        }

        test("registration is successful, expect no error response") {
            // arrange
            every {
                authResultTaskMock.isSuccessful
            } returns true

            every {
                authResultTaskMock.result
            } returns mockk()

            // act
            val result = firebaseAuthFacade.registerWithEmailAndPassword(
                EmailAddress.create("john@gmail.com"),
                Password.create("12345678")
            )

            // assert
            expectThat(result).isRight(Unit)

            verify(exactly = 1) {
                authMock.createUserWithEmailAndPassword(any(), any())
            }

            expect {
                that(emailSlot.captured) isEqualTo "john@gmail.com"
                that(passwordSlot.captured) isEqualTo "12345678"
            }

        }

        withData<Pair<Exception?, AuthFailure>>(
            nameFn = { "registration is unsuccessful with ${it.first}, expect error response with ${it.second} failure" },
            null to AuthFailure.ServerError,
            FirebaseAuthException("FAKE", "fake auth exception") to AuthFailure.ServerError,
            FirebaseAuthUserCollisionException("FAKE", "fake user collision exception") to AuthFailure.EmailAlreadyInUse
        ) { (exception, failure) ->
            // arrange
            every {
                authResultTaskMock.isSuccessful
            } returns false

            every {
                authResultTaskMock.exception
            } returns exception

            // act
            val result = firebaseAuthFacade.registerWithEmailAndPassword(
                EmailAddress.create("john@gmail.com"),
                Password.create("12345678")
            )

            // assert
            expectThat(result).isLeft(failure)

            verify(exactly = 1) {
                authMock.createUserWithEmailAndPassword(any(), any())
            }

            expect {
                that(emailSlot.captured) isEqualTo "john@gmail.com"
                that(passwordSlot.captured) isEqualTo "12345678"
            }
        }

    }

    context("signInWithEmailAndPassword is called") {
        val emailSlot = slot<String>()
        val passwordSlot = slot<String>()

        val authResultTaskMock: Task<AuthResult> = mockk()

        every {
            authMock.signInWithEmailAndPassword(
                capture(emailSlot),
                capture(passwordSlot)
            )
        } returns authResultTaskMock

        val listenerSlot: CapturingSlot<OnCompleteListener<AuthResult>> = slot()

        every {
            authResultTaskMock
                .addOnCompleteListener(capture(listenerSlot))
        } answers {
            listenerSlot.captured.onComplete(authResultTaskMock)
            mockk()
        }

        test("sign in is successful, expect no error response") {
            // arrange
            every {
                authResultTaskMock.isSuccessful
            } returns true

            every {
                authResultTaskMock.result
            } returns mockk()

            // act
            val result = firebaseAuthFacade.signInWithEmailAndPassword(
                EmailAddress.create("john@gmail.com"),
                Password.create("12345678")
            )

            // assert
            expectThat(result).isRight(Unit)

            verify(exactly = 1) {
                authMock.signInWithEmailAndPassword(any(), any())
            }

            expect {
                that(emailSlot.captured) isEqualTo "john@gmail.com"
                that(passwordSlot.captured) isEqualTo "12345678"
            }

        }

        withData<Pair<Exception?, AuthFailure>>(
            nameFn = { "sign in is unsuccessful with ${it.first}, expect error response with ${it.second} failure" },
            null to AuthFailure.ServerError,
            FirebaseAuthInvalidUserException("FAKE", "fake invalid user exception") to AuthFailure.InvalidEmailAndPasswordCombination,
            FirebaseAuthInvalidCredentialsException("FAKE", "fake invalid credential exception") to AuthFailure.InvalidEmailAndPasswordCombination
        ) { (exception, failure) ->
            // arrange
            every {
                authResultTaskMock.isSuccessful
            } returns false

            every {
                authResultTaskMock.exception
            } returns exception

            // act
            val result = firebaseAuthFacade.signInWithEmailAndPassword(
                EmailAddress.create("john@gmail.com"),
                Password.create("12345678")
            )

            // assert
            expectThat(result).isLeft(failure)

            verify(exactly = 1) {
                authMock.signInWithEmailAndPassword(any(), any())
            }

            expect {
                that(emailSlot.captured) isEqualTo "john@gmail.com"
                that(passwordSlot.captured) isEqualTo "12345678"
            }
        }

    }

    context("signInWithGoogle is called") {

        test("sign in is successful, expect no error response") {
            // act
            val result = firebaseAuthFacade.signInWithGoogle()

            // assert
            expectThat(result).isRight(Unit)
        }
    }
})
