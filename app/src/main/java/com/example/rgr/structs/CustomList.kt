    package com.example.rgr.structs

    import java.io.Serializable

    class CustomList<T : Comparable<T>> : Serializable {
        private var len = 0
        private var head: Node<T>? = null

        constructor() {
            head = null
        }

        constructor(value: T) {
            head = null
            push(value)
        }

        fun push(value: T) {
            if (head == null) {
                head = Node(value)
            } else {
                var tmp = head

                while (tmp?.next != null) {
                    tmp = tmp.next
                }
                tmp?.next = Node(value)
            }
            len++
        }

        fun pop(ind: Int): T? {
            val res: T? = null
            if (head != null) {
                var tmp = head
                var prev = head
                for (i in 0 until len) {
                    if (i == ind) {
                        if (tmp == head)
                            head = tmp?.next
                        else
                            prev?.next = tmp?.next
                        len--
                        return tmp?.value
                    }
                    prev = tmp
                    tmp = tmp?.next
                }
            }
            return res
        }

        fun insert(ind: Int, value: T) {
            if (ind <= len) {
                if (ind == 0) {
                    val tmp = Node(value)
                    tmp.next = head
                    head = tmp
                    len++
                } else {
                    var tmp = head
                    var prev = head
                    var i = 0
                    while (tmp != null) {
                        if (i == ind) {
                            val newNode = Node(value)
                            prev?.next = newNode
                            newNode.next = tmp
                            len++
                            return
                        }
                        prev = tmp
                        tmp = tmp.next
                        i++
                    }
                }
            }
        }

        fun forEach(callback: ForEachCallbackInterface<T>) {
            var tmp = head

            while (tmp != null) {
                tmp.value?.let { callback.toDo(it) }
                tmp = tmp.next
            }
        }

        fun getElement(index: Int): T {
            if (index in 0 until len) {
                var tmp = head
                var i = 0
                while (tmp != null) {
                    if (i == index)
                        return tmp.value!!
                    tmp = tmp.next
                    i++
                }
            }
            throw IndexOutOfBoundsException("Index out of bounds: $index")
        }

        fun setElement(ind: Int, element: T) {
            if (head != null) {
                if (ind < len) {
                    var tmp = head
                    var i = 0
                    while (tmp != null) {
                        if (i == ind) {
                            tmp.value = element
                            return
                        }
                        i++
                        tmp = tmp.next
                    }
                } else {
                    throw IndexOutOfBoundsException("Index out of bounds")
                }
            }
        }

        fun print() {
            if (head != null) {
                var tmp = head
                while (tmp != null) {
                    print(tmp.value)
                    print(" -> ")
                    tmp = tmp.next
                }
            }
            println(" |Len = $len")
        }

        override fun toString(): String {
            val builder = StringBuilder()

            var current = head
            while (current != null) {
                builder.append(current.value)
                if (current.next != null) {
                    builder.append(" -> ")
                }
                current = current.next
            }
            builder.append(" >>")
            return builder.toString()
        }

        fun getLength(): Int {
            return len
        }

        fun isEmpty(): Boolean {
            return head == null
        }
    }