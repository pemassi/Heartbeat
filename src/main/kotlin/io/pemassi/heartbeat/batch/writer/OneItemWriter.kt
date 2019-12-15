package io.pemassi.heartbeat.batch.writer

interface OneItemWriter<T>
{
    fun write(item: T)
}