package com.example.repository

import com.example.domain.entity.Task
import com.example.domain.enum.Priority
import com.example.domain.repository.ITaskRepository
import com.mongodb.MongoException
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.BsonValue
import org.bson.types.ObjectId
class TaskRepositoryImpl(
    private val mongoDatabase: MongoDatabase
) : ITaskRepository {

    companion object {
        const val TASK_COLLECTION = "task"
    }

    override suspend fun allTasks(): List<Task> {
        try {
            return mongoDatabase.getCollection<Task>(TASK_COLLECTION)
                .find()
                .toList()
        } catch (e: MongoException) {
            System.err.println("Unable to retrieve data due to an error: $e")
        }
        return emptyList()
    }

    override suspend fun addTask(task: Task): BsonValue? {
        try {
            return mongoDatabase.getCollection<Task>(TASK_COLLECTION)
                .insertOne(
                    task
                ).insertedId
        } catch (e: MongoException) {
            System.err.println("Unable to insert data due to an error: $e")
        }
        return null

    }

    override suspend fun updateTask(id: ObjectId, task: Task): Long? {
        try {
            val updates = Updates.combine(
                Updates.set(Task::name.name, task.name),
                Updates.set(Task::description.name, task.description),
                Updates.set(Task::priority.name, task.priority)
            )

            val options = UpdateOptions().upsert(true)

            return mongoDatabase.getCollection<Task>(TASK_COLLECTION).updateOne(
                eq(
                    Task::id.name,
                    id
                ),
                updates,
                options
            ).modifiedCount
        } catch (e: MongoException) {
            System.err.println("Unable to update data due to an error: $e")
        }
        return null
    }

    override suspend fun removeTask(id: ObjectId): Boolean {
        try {
            return mongoDatabase.getCollection<Task>(TASK_COLLECTION).deleteOne(
                eq(
                    Task::id.toString(),
                    id
                )
            ).deletedCount > 0
        } catch (e: MongoException) {
            System.err.println("Unable to remove data due to an error: $e")
        }
        return false
    }

    override suspend fun findById(id: ObjectId): Task? {
        try {
            return mongoDatabase.getCollection<Task>(TASK_COLLECTION)
                .find(eq(Task::id.name, id))
                .limit(1)
                .firstOrNull()
        } catch (e: MongoException) {
            System.err.println("Unable to find data due to an error: $e")
        }
        return null
    }

    override suspend fun findByPriority(priority: Priority): List<Task> {
        try {
            return mongoDatabase.getCollection<Task>(TASK_COLLECTION)
                .find(
                    eq(
                        Task::priority.name,
                        priority
                    )
                )
                .toList()
        } catch (e: MongoException) {
            System.err.println("Unable to find data due to an error: $e")
        }
        return emptyList()
    }

    override suspend fun findByName(name: String): Task? {
        try {
            return mongoDatabase.getCollection<Task>(TASK_COLLECTION)
                .find<Task>(eq(Task::name.name, name))
                .limit(1)
                .firstOrNull()
        } catch (e: MongoException) {
            System.err.println("Unable to find data due to an error: $e")
        }
        return null
    }
}
