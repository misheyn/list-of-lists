package com.example.rgr

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rgr.structs.ListOfLists
import android.widget.*
import com.example.rgr.structs.GPS
import com.example.rgr.structs.Serializer
import com.example.rgr.ui.theme.RGRTheme
import java.io.*

class MainActivity : ComponentActivity() {

    private val KEY_LIST_OF_INT = "keyListOfInt"
    private val KEY_LIST_OF_GPS = "keyListOfGPS"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_LIST_OF_INT, listOfListsInt)
        outState.putSerializable(KEY_LIST_OF_GPS, listOfListsGPS)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        listOfListsInt = savedInstanceState.get<ListOfLists<Int>>(KEY_LIST_OF_INT) ?: ListOfLists<Int>()
        listOfListsGPS = savedInstanceState.get<ListOfLists<GPS>>(KEY_LIST_OF_GPS) ?: ListOfLists<GPS>()
        updateTextViewContent()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTextViewContent() {
        val isGPSMode = findViewById<Switch>(R.id.switchGPS).isChecked
        val textViewResult = findViewById<TextView>(R.id.textViewResult)
        if (isGPSMode) {
            textViewResult.text = "Content: $listOfListsGPS"
        } else {
            textViewResult.text = "Content: $listOfListsInt"
        }
    }

    private var listOfListsInt = ListOfLists<Int>()
    private var listOfListsGPS = ListOfLists<GPS>()

    val fileNameMap = mapOf(
        true to "saveDataGPS.dat",
        false to "saveDataInt.dat"
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editTextValue = findViewById<EditText>(R.id.editTextValue)
        val editTextIndex = findViewById<EditText>(R.id.editTextIndex)
        val textViewResult = findViewById<TextView>(R.id.textViewResult)
        val spinnerOptions = findViewById<Spinner>(R.id.spinnerOptions)
        val switchGPS = findViewById<Switch>(R.id.switchGPS)

        val options = arrayOf(
            "Push",
            "Pop",
            "Insert",
            "Get by id",
            "Sort",
            "Balancing",
            "Save to file",
            "Import from file"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOptions.adapter = adapter

        spinnerOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedOption = options[position]
                editTextValue.isEnabled = false
                editTextIndex.isEnabled = false
                when (selectedOption) {
                    "Push" -> {
                        editTextValue.isEnabled = true
                    }

                    "Pop" -> {
                        editTextValue.isEnabled = false
                        editTextIndex.isEnabled = true
                    }

                    "Insert" -> {
                        editTextValue.isEnabled = true
                        editTextIndex.isEnabled = true
                    }

                    "Get by id" -> {
                        editTextIndex.isEnabled = true
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // do nothing new
            }

        }
        switchGPS.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    textViewResult.text = "Content: $listOfListsGPS"
                    switchGPS.text = "GPS"
                }
                else {
                    textViewResult.text = "Content: $listOfListsInt"
                    switchGPS.text = "Int"
                }
            }
        }
        val buttonExecute = findViewById<Button>(R.id.buttonExecute)
        buttonExecute.setOnClickListener {
            val selectedOption = options[spinnerOptions.selectedItemPosition]
            val isGPSMode = switchGPS.isChecked
            val fileName = fileNameMap[isGPSMode] ?: fileNameMap[false]!! // elvis operator" (check null)


            when (selectedOption) {
                "Save to file" -> {
                    try {
                        if (isGPSMode) {
                            Serializer.write(this.applicationContext, listOfListsGPS, fileName)
                            textViewResult.text = "Content: $listOfListsGPS"
                        } else {
                            Serializer.write(this.applicationContext, listOfListsInt, fileName)
                            textViewResult.text = "Content: $listOfListsInt"
                        }
                        showToast("Data saved to file.")
                    } catch (e: IOException) {
                        e.printStackTrace()
                        showToast("Error saving data to file.")
                    }
                }

                "Import from file" -> {
                    try {
                        if (isGPSMode) {
                            listOfListsGPS = Serializer.read(this.applicationContext, fileName) as ListOfLists<GPS>
                            textViewResult.text = "Content: $listOfListsGPS"
                        } else {
                            listOfListsInt = Serializer.read(this.applicationContext, fileName) as ListOfLists<Int>
                            textViewResult.text = "Content: $listOfListsInt"
                        }
                        showToast("Data imported from file.")
                    } catch (e: Exception) {
                        when (e) {
                            is IOException, is ClassNotFoundException ->
                                showToast("Error importing data from file.")

                            else -> throw e
                        }
                    }
                }

                "Push" -> {
                    val numbers = editTextValue.text.toString().split(" ")
                    if (isGPSMode) {
                        if (numbers.size == 2) {
                            val firstNumber = numbers[0].toDoubleOrNull()
                            val secondNumber = numbers[1].toDoubleOrNull()
                            if (firstNumber != null && secondNumber != null) {
                                listOfListsGPS.push(GPS(firstNumber, secondNumber))
                                textViewResult.text = "Content: $listOfListsGPS"
                            } else {
                                showToast("Invalid values")
                            }
                        } else {
                            showToast("Invalid values")
                        }
                    } else {
                        if (numbers.size == 1) {
                            val value = numbers[0].toIntOrNull()
                            if (value != null) {
                                listOfListsInt.push(value)
                                textViewResult.text = "Content: $listOfListsInt"
                            } else {
                                showToast("Invalid value")
                            }
                        } else {
                            showToast("Invalid value")
                        }
                    }
                }

                "Pop" -> {
                    try {
                        val index = editTextIndex.text.toString().toIntOrNull()
                        if (index != null) {
                            if (isGPSMode) {
                                listOfListsGPS.pop(index)
                                textViewResult.text = "Content: $listOfListsGPS"
                            } else {
                                listOfListsInt.pop(index)
                                textViewResult.text = "Content: $listOfListsInt"
                            }
                        } else {
                            showToast("Invalid index")
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        showToast("Invalid index")
                    } catch (e: NullPointerException) {
                        showToast("List is empty")
                    }
                }

                "Insert" -> {
                    val index = editTextIndex.text.toString().toIntOrNull()
                    try {
                        if (index != null) {
                            val numbers = editTextValue.text.toString().split(" ")
                            if (isGPSMode) {
                                if (numbers.size == 2) {
                                    val firstNumber = numbers[0].toDoubleOrNull()
                                    val secondNumber = numbers[1].toDoubleOrNull()
                                    if (firstNumber != null && secondNumber != null) {
                                        listOfListsGPS.insert(index, GPS(firstNumber, secondNumber))
                                        textViewResult.text = "Content: $listOfListsGPS"
                                    } else {
                                        showToast("Invalid values")
                                    }
                                } else {
                                    showToast("Invalid values")
                                }
                            } else {
                                if (numbers.size == 1) {
                                    val value = numbers[0].toIntOrNull()
                                    if (value != null) {
                                        listOfListsInt.insert(index, value)
                                        textViewResult.text = "Content: $listOfListsInt"
                                    } else {
                                        showToast("Invalid value")
                                    }
                                } else {
                                    showToast("Invalid value")
                                }
                            }
                            textViewResult.text = "Content: $listOfListsInt"
                        } else {
                            showToast("Invalid index")
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        showToast("Invalid index")
                    }
                }

                "Sort" -> {
                    try {
                        if (isGPSMode) {
                            listOfListsGPS.sort()
                            textViewResult.text = "Content: $listOfListsGPS"
                        } else {
                            listOfListsInt.sort()
                            textViewResult.text = "Content: $listOfListsInt"
                        }
                    } catch (e: NullPointerException) {
                        showToast("List is empty")
                    }
                }

                "Get by id" -> {
                    val index = editTextIndex.text.toString().toIntOrNull()
                    try {
                        if (index != null) {
                            val element: Any
                            element = if (isGPSMode)
                                listOfListsGPS.getElement(index)
                            else
                                listOfListsInt.getElement(index)

                            textViewResult.text = "Element at index $index: $element"
                        } else {
                            showToast("Invalid index")
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        showToast("Invalid index")
                    } catch (e: NullPointerException) {
                        showToast("List is empty")
                    }
                }

                "Balancing" -> {
                    try {
                        if (isGPSMode) {
                            listOfListsGPS.balancing()
                            textViewResult.text = "Content: $listOfListsGPS"
                        } else {
                            listOfListsInt.balancing()
                            textViewResult.text = "Content: $listOfListsInt"
                        }
                    } catch (e: NullPointerException) {
                        showToast("List is empty")
                    }
                }

            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inline fun <reified T : Any> Bundle.get(key: String): T? {
        return get(key) as? T
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RGRTheme {
        Greeting("Android")
    }
}