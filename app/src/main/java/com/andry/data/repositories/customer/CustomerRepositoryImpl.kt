package com.andry.data.repositories.customer

//@Singleton
//class CustomerRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
//    CustomerRepository {
//
//    private suspend fun getCustomers(): Response<List<Customer>> {
//        return safeApiCall { apiService.getCustomers() }
//    }
//
//    override suspend fun getCustomersFromDatabase(): List<Customer>? {
//        return withContext(Dispatchers.IO) {
//            val db = Room.databaseBuilder(
//                context,
//                AppDatabase::class.java, "app_database"
//            )
//                .fallbackToDestructiveMigration()
//                .build()
//
//            val customerDao = db.customerDao()
//            val apiResponse = getCustomers()
//
//            if (apiResponse.isSuccessful) {
//                customerDao.deleteAll()
//                apiResponse.body()?.let {
//                    customerDao.insertAll(it)
//                }
//            }
//
//            val customersFromDao = customerDao.getAll()
//
//            Log.d("CustomerRepository", "Customer from dao: $customersFromDao")
//            return@withContext customersFromDao
//        }
//    }
//}