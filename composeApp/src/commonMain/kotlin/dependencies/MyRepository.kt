package dependencies

interface MyRepository {
    fun helloWorld(): String
    fun getDevice(): String
}

class MyRepositoryImpl(
    private val dbClient: DbClient
) : MyRepository {
    override fun helloWorld(): String {
        return "Hello World!"
    }
    override fun getDevice(): String = getDeviceName()

}
expect fun getDeviceName(): String