/*
 * *
 *  * Copyright (c) 2014 Antoine Jullien
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.cirrus.utils;

public abstract class Try<R> {

    /**
     * Returns the either the result, either an error
     */
    public abstract R get() throws Exception;

    public static <E> Try<E> success(final E e) {
        return new Success<E>(e);
    }

    public static <E> Try<E> failure(final Exception e) {
        return new Failure<E>(e);
    }


    static class Success<R> extends Try<R> {

        //==================================================================================================================
        // Attributes
        //==================================================================================================================
        private final R result;

        //==================================================================================================================
        // Constructors
        //==================================================================================================================
        Success(final R result) {
            super();
            this.result = result;
        }

        //==================================================================================================================
        // Public
        //==================================================================================================================
        @Override
        public R get() {
            return this.result;
        }
    }

    static class Failure<E> extends Try<E> {
        //==================================================================================================================
        // Attributes
        //==================================================================================================================
        private final Exception throwable;

        //==================================================================================================================
        // Constructors
        //==================================================================================================================
        Failure(final Exception throwable) {
            this.throwable = throwable;
        }

        //==================================================================================================================
        // Public
        //==================================================================================================================
        @Override
        public E get() throws Exception {
            throw throwable;
        }
    }

}
