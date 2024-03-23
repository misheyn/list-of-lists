package com.example.rgr.structs

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class ListOfLists<T : Comparable<T>> : Serializable {
    private var maxLen = 2
    private var head: Node<CustomList<T>>? = null
    private var len = 0

    constructor() {
        head = null
    }

    constructor(maxLen: Int) {
        this.maxLen = maxLen
    }

    fun push(value: T) {
        if (head == null) {
            head = Node()
            head?.value = CustomList(value)
            len++
        } else {
            var tmp = head
            var prev = head

            while (tmp != null) {
                if (tmp.value?.getLength()!! < maxLen) {
                    tmp.value?.push(value)
                    len++
                    return
                }
                prev = tmp
                tmp = tmp.next
            }
            prev?.next = Node()
            prev?.next?.value = CustomList(value)
            len++
        }
    }

    fun pop(index: Int): T? {
        val res: T?
        var ind = index
        if (head != null) {
            var tmp = head
            var prev = head
            while (tmp != null) {
                if (ind < tmp.value?.getLength()!!) {
                    res = tmp.value?.pop(ind)
                    if (tmp.value?.isEmpty()!!) {
                        if (tmp == head)
                            head = tmp.next
                        else
                            prev?.next = tmp.next
                    }
                    len--
                    return res
                }
                ind -= tmp.value?.getLength()!!
                prev = tmp
                tmp = tmp.next
            }
            throw IndexOutOfBoundsException("Index out of bounds: $ind")
        } else {
            throw NullPointerException()
        }
    }

    fun insert(index: Int, value: T) {
        if (index == 0) {
            if (head == null) {
                head = Node()
                head?.value = CustomList(value)
            } else {
                head?.value?.insert(index, value)
                balancing(head!!)
            }
            len++
        } else if (index >= len) {
            throw IndexOutOfBoundsException("Index out of bounds: $index")
        } else {
            var ind = index
            var tmp = head
            while (tmp != null) {
                if (ind < tmp.value?.getLength()!!) {
                    tmp.value?.insert(ind, value)
                    balancing(tmp)
                    len++
                    return
                }
                ind -= tmp.value?.getLength()!!
                tmp = tmp.next
            }
        }
    }

    fun balancing() {
        if (head == null)
            throw NullPointerException()
        var tmp = head
        var prev = head
        while (tmp != null) {
            while (tmp.value?.getLength()!! < maxLen && tmp.next != null && tmp.next?.value != null) {
                tmp.value?.push(tmp.next!!.value!!.pop(0)!!)
            }
            if (tmp.next == null && tmp.value?.getLength() == 0)
                prev?.next = null
            prev = tmp
            tmp = tmp.next
        }
    }

    private fun balancing(node: Node<CustomList<T>>) {
        var currentNode: Node<CustomList<T>>? = node
        while (currentNode != null) {
            if (currentNode.value?.getLength()!! > maxLen) {
                val value = currentNode.value?.pop(currentNode.value?.getLength()!! - 1)!!
                if (currentNode.next != null) {
                    currentNode.next?.value?.insert(0, value)
                } else {
                    currentNode.next = Node()
                    currentNode.next?.value = CustomList(value)
                }
            }
            currentNode = currentNode.next
        }
    }

    fun forEach(callback: ForEachCallbackInterface<T>) {
        var tmp = head

        while (tmp != null) {
            val customList = tmp.value
            customList?.forEach(callback)
            tmp = tmp.next
        }
    }

    fun sort() {
        if (head == null)
            throw NullPointerException()
        for (i in len / 2 - 1 downTo 0) {
            heapify(len, i)
        }

        for (i in len - 1 downTo 0) {
            val swap = getElement(0)
            setElement(0, getElement(i))
            setElement(i, swap)
            heapify(i, 0)
        }
        balancing()
    }

    private fun heapify(n: Int, i: Int) {
        var largest = i
        val l = 2 * i + 1
        val r = 2 * i + 2

        if (l < n && getElement(l) > getElement(largest))
            largest = l

        if (r < n && getElement(r) > getElement(largest))
            largest = r
        if (largest != i) {
            swapElements(i, largest)
            heapify(n, largest)
        }
    }

    private fun swapElements(first: Int, second: Int) {
        val swap = getElement(first)
        setElement(first, getElement(second))
        setElement(second, swap)
    }

    private fun setElement(index: Int, element: T) {
        if (head != null) {
            var tmp = head
            var ind = index
            while (tmp != null) {
                if (ind < tmp.value?.getLength()!!) {
                    tmp.value?.setElement(ind, element)
                    return
                }
                ind -= tmp.value?.getLength()!!
                tmp = tmp.next
            }
            throw IndexOutOfBoundsException("Index out of bounds: $ind")
        }
    }

    fun getElement(index: Int): T {
        if (head != null) {
            var tmp = head
            var ind = index
            while (tmp != null) {
                if (ind < tmp.value?.getLength()!!) {
                    return tmp.value!!.getElement(ind)
                }
                ind -= tmp.value?.getLength()!!
                tmp = tmp.next
            }
            throw IndexOutOfBoundsException("Index out of bounds: $ind")
        } else {
            throw NullPointerException()
        }
    }

    fun print() {
        if (head != null) {
            println("List of lists")
            var tmp = head

            while (tmp != null) {
                tmp.value?.print()
                tmp = tmp.next
            }
        } else {
            println("List if lists is empty")
        }
    }

    override fun toString(): String {
        val builder = StringBuilder("ListOfLists:\n")

        var current = head
        while (current != null) {
            current.value?.let {
                builder.append(it.toString())
            }
            builder.append("\n")

            current = current.next
        }

        return builder.toString()
    }

    companion object CREATOR : Parcelable.Creator<GPS> {
        override fun createFromParcel(parcel: Parcel): GPS {
            return GPS(parcel)
        }

        override fun newArray(p0: Int): Array<GPS?> {
            return arrayOfNulls(p0);
        }
    }
}