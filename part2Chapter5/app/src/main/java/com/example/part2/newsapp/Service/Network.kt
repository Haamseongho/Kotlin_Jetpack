package com.example.part2.newsapp.Service

class Network {
    private var service: Service? = null
    companion object {
        @Volatile
        private var instance: Network? = null

        fun getInstance(): Network {
            return instance ?: synchronized(this){
                instance ?: Network().also { instance = it }
            }
        }
    }

    init {
        service = ApiConstant.retrofit.create(Service::class.java)
    }

    fun getService(): Service {
        return service!!
    }
}