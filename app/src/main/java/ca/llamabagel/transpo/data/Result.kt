/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

/**
 * A generic class that holds a value with its loading status.
 * @param T
 */
sealed class Result<out T : Any>(open val data: T? = null) {

    class Success<out T : Any>(data: T) : Result<T>(data) {
        override val data: T
            get() = super.data!!
    }
    class Loading<out T : Any>(data: T? = null) : Result<T>(data)
    class Error<out T : Any>(val exception: Exception, data: T? = null) : Result<T>(data)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error<*> -> "Error[exception=$exception, data=$data]"
            is Loading<*> -> "Loading[oldData=$data]"
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Result<*> &&
                other.data == data
    }

    override fun hashCode(): Int = data?.hashCode() ?: 0
}