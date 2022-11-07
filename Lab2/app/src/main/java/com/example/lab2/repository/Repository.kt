package com.example.lab2.repository

import com.example.lab2.domain.Horse

class Repository internal constructor() {
    private var horses: MutableList<Horse> = ArrayList()
    var index: Int = 0

    init {
        insertMockData()
    }

    fun getHorses(): MutableList<Horse> {
        return horses
    }

    fun insert(horse: Horse) {
        var duplicate: Horse? = searchById(horse.id)
        if (duplicate == null) {
            horse.id = index
            horses.add(horse)
            index += 1
        }
        else {
            duplicate = horse
        }
    }

    fun delete(id: Int) {
        val horse: Horse? = searchById(id)
        if (horse != null) {
            horses.remove(horse)
        }
    }

    fun searchById(id: Int) : Horse? {
        horses.forEach {
            if (it.id == id) {
                return it
            }
        }
        return null
    }

    private fun insertMockData() {
        insert(Horse(-1, "Caesar", "Lipizzaner", "male", "black", 15))
        insert(Horse(-1, "Sultan", "Andalusian", "male", "black", 15))
        insert(Horse(-1, "Monica", "Lipizzaner", "male", "black", 15))
        insert(Horse(-1, "Conte", "Andalusian", "male", "black", 15))
        insert(Horse(-1, "Nazir", "Friesian", "male", "black", 15))
        insert(Horse(-1, "Shrek", "Lipizzaner", "male", "black", 15))
        insert(Horse(-1, "Narcis", "Andalusian", "male", "black", 15))
        insert(Horse(-1, "Nadia", "Friesian", "male", "black", 15))
        insert(Horse(-1, "Joy", "Friesian", "male", "black", 15))
    }
}

var repo: Repository = Repository()