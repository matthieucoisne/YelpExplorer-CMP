package cmp.yelpexplorer.core.utils

interface Mapper<INPUT, OUTPUT> {
    suspend fun map(input: INPUT): OUTPUT
}
