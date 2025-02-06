package hu.ait.httpdemo.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoneyRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CityRetrofit