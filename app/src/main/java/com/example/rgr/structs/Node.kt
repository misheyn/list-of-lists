package com.example.rgr.structs

import java.io.Serializable

class Node<T> : Serializable {
    var value: T? = null
    var next: Node<T>? = null

    constructor() {
        value = null
        next = null
    }

    constructor(value: T) {
        this.value = value
        next = null
    }
}