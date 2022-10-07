package book.com.book

import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Query
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping ("/mony")
class HelloController {

    private val log = LoggerFactory.getLogger(this::class.java)
// to add the sth into file must be mutable list


//
//    var fictionBookList = mutableListOf(
//        Book(1, "Harry Potter  ", "JK", categorize = Categorize()),
//        Book(2, "Peaky Blinder ", "JC", categorize = Categorize())
//    )
//
//    var nonFictionBookList = mutableListOf(
//        Book(3, "Demon Slayer  ", "JM", categorize = Categorize()),
//        Book(4, "Attack in Titan ","JA", categorize = Categorize()),
//
//    )
//
//    val bookList = fictionBookList + nonFictionBookList
//
//    val nonFictionCategory = Categorize("Non-Fiction", "black", books = nonFictionBookList)
//    val fictionCategory = Categorize("Fiction", "black", books = fictionBookList)
//
//    val categorizeList: List<Categorize> = listOf(nonFictionCategory, fictionCategory)



    val nonFictionCategory = Categorize(1, "Non-Fiction", "black")
    val fictionCategory = Categorize(2, "Fiction", "black")

    val categorizeList: List<Categorize> = listOf(nonFictionCategory, fictionCategory)


    var fictionBookList = mutableListOf(
        Book(1, "Harry Potter  ", "JK",  2018, categorize = nonFictionCategory),
        Book(2, "Peaky Blinder ", "JC",2017, categorize = nonFictionCategory)

    )

    var nonFictionBookList = mutableListOf(
        Book(3, "Demon Slayer  ", "JM", 2020, categorize = fictionCategory),
        Book(4, "Attack in Titan ","JA", 2022, categorize = fictionCategory),

        )
//this booklist must be immutable ,because they doesn't have to add in
    var bookList = fictionBookList + nonFictionBookList

// sort the pushdate
    val sortedList = bookList.sortedByDescending { it.publishdate }




    @GetMapping("/categories/{id}")
    fun getAllBooksByCategory(@PathVariable("id") categoryId: Int): Total{
        val foundBooks = bookList.filter { it.categorize.id == categoryId }



        //println("There is total ${foundBooks.count()} books")

        return Total(
            data = foundBooks,
            message = "The total is ${foundBooks.count()}"
        )
    }




    @GetMapping ("/welcome")
    fun welcome(): String {
        return "'HI Mony'"
    }



// endpoint books
//Get book by using data class Response
    @GetMapping ("/books")
// Response here is just the data class to when we have more than object (data and msg:string)
    fun getBooks (): Response{
        return Response(
            data = bookList,
            message = "Get all the books successfully"
        )
    }
//Get book without using the data class Reponse & as a string
    @GetMapping ("/wm")
    fun getanotherBook (): List<Book> {
        return bookList
    }


// get book by specific id

    @GetMapping ("/books/{bookId}")
//PathVariable does for get the specific int ,long, float

    fun getBookById (@PathVariable("bookId") bookId: Int) : String {

// set book to loop( find last match id ) from bookList
        val book = bookList.findLast {it.id == bookId}

// set false case when we wanna use condition
        var isIdFound= false

        for (book in bookList)
            if (book.id == bookId){
                isIdFound = true
            }

        // if isIdFound is true then ...
        if(isIdFound){
            return Message(
                data = null,
                message = bookList.toString() + "not found"
            ).toString()


        }else{
            return Message (
                data = null,
                message = bookList.toString() + "found"
                    ).toString()

        }

    }



    @GetMapping ("/categorize")
    fun getCategorize (): List<Categorize> {
        return categorizeList
    }



// More secure when you wanna add on and nobody can see what are the insides
    @PostMapping ("/post")

// Request to edit for the body part
    fun postBook (@RequestBody name: Book) : List<Book> {
        log.info("RequestBody: $name")

        bookList = bookList.plus(name)

        return bookList
    }


    @PutMapping ("/books/{id}")
    fun putBook (@PathVariable id: Int, @RequestBody book: Book) : String{
        //reset book.id
        val findBook = bookList.find { it -> it.id == id }


//        var foundBook: Book?
//        for (book in bookList) {
//            if (book.id == id) {
//                foundBook = book
//                break
//            }
//        }

        if (findBook != null) {

            val newList = bookList.filter { it.id != id }.plus(book)

            bookList = newList.toMutableList()

//            bookList.forEachIndexed { index, existedBook ->
//                if (existedBook.id == id) bookList[index] = book
//            }
            return "Book has been updated"
        } else {
            return "Book not found"
        }



//        bookList.forEachIndexed { index, existedBook ->
//            if (existedBook.id == id) bookList[index] = book
//        }
//        return "Book has been updated"
    }
// Delete the books
    @DeleteMapping ("/books/delete/{id}")
   fun deleteBook (@PathVariable id: Int): String {





//    for (book in bookList.withIndex()) {
//
//
//
////        if (book.id == id) {
//////            bookList.removeAt(index)
////        }
//    }


//    for ((index, book) in bookList.withIndex()) {
//        if (book.id == id) {
//            bookList.removeAt(index)
//            break
//        }
//    }

    return "Delete successfully"

//       return bookList.removeAt()
   }



}
// data class to add the string into list of books
data class Response(
    // data for list
    val data: List<Book>,
    // msg for string that we wanna add
    val message: String
)

data class Book(
    val id: Int,
    val name: String,
    val author: String,
    val publishdate:Int,
    val categorize: Categorize

//categorize has Categorize class

)
data class Categorize (
    val id: Int,
    val type: String,
    val color: String
)

data class Message(
    val data: Book?,
    val message:String
)

data class Total(
    val data: List<Book>,
    val message: String
)


