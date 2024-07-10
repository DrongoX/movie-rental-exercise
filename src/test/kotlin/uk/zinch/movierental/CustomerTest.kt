package uk.zinch.movierental;

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import uk.zinch.movierental.Customer.Movie
import uk.zinch.movierental.Customer.Tape

class CustomerTest : DescribeSpec({
	it("should print statement without rentals") {

		val customer = Customer("El moatamid")
		val result = customer.statement()

		result shouldBe "Rental Record for El moatamid\n" +
				"Amount owed is 0.0\n" +
				"You earned 0 frequent renter points"
	}

	it("should print statement for renting a children movie for one day") {
		val customer = Customer("El moatamid")
		val movie = Movie("Tom & jerry", 2)
		val tape = Tape("Serail_Number", movie)
		val rental = Customer.Rental(tape, 1)
		customer.addRental(rental)

		val result = customer.statement()

		result shouldBe "Rental Record for El moatamid\n" +
				"\tTom & jerry\t1.5\n" +
				"Amount owed is 1.5\n" +
				"You earned 1 frequent renter points"
	}

	it("should print statement for renting a children movie for four days") {
		val customer = Customer("El moatamid")
		val movie = Movie("Tom & jerry", 2)
		val tape = Tape("Serail_Number", movie)
		val rental = Customer.Rental(tape, 4)
		customer.addRental(rental)

		val result = customer.statement()

		result shouldBe "Rental Record for El moatamid\n" +
				"\tTom & jerry\t3.0\n" +
				"Amount owed is 3.0\n" +
				"You earned 1 frequent renter points"
	}

	it("should print statement for renting a regular movie for one day") {
		val customer = Customer("El moatamid")
		val movie = Movie("The Godfather", 0)
		val tape = Tape("Serail_Number", movie)
		val rental = Customer.Rental(tape, 1)
		customer.addRental(rental)

		val result = customer.statement()

		result shouldBe "Rental Record for El moatamid\n" +
				"\tThe Godfather\t2.0\n" +
				"Amount owed is 2.0\n" +
				"You earned 1 frequent renter points"
	}

	it("should print statement for renting a regular movie for more than two days") {
		val customer = Customer("El moatamid")
		val movie = Movie("The Godfather", 0)
		val tape = Tape("Serail_Number", movie)
		val rental = Customer.Rental(tape, 4)
		customer.addRental(rental)

		val result = customer.statement()

		result shouldBe "Rental Record for El moatamid\n" +
				"\tThe Godfather\t5.0\n" +
				"Amount owed is 5.0\n" +
				"You earned 1 frequent renter points"
	}

	it("should print statement for renting a new_release movie for one day") {
		val customer = Customer("El moatamid")
		val movie = Movie("Always wrong", 1)
		val tape = Tape("Serail_Number", movie)
		val rental = Customer.Rental(tape, 1)
		customer.addRental(rental)

		val result = customer.statement()

		result shouldBe "Rental Record for El moatamid\n" +
				"\tAlways wrong\t3.0\n" +
				"Amount owed is 3.0\n" +
				"You earned 1 frequent renter points"
	}

	it("should print statement with multiple tapes") {
		val customer = Customer("El moatamid")
		val movie = Movie("Always wrong", 1)
		val tape = Tape("Serail_Number", movie)
		val rental = Customer.Rental(tape, 1)

		val movie2 = Movie("The Godfather", 1)
		val tape2 = Tape("Serail_Number2", movie2)
		val rental2 = Customer.Rental(tape2, 4)

		customer.addRental(rental)
		customer.addRental(rental2)

		val result = customer.statement()
		result shouldBe "Rental Record for El moatamid\n" +
				"\tAlways wrong\t3.0\n" +
				"\tThe Godfather\t12.0\n" +
				"Amount owed is 15.0\n" +
				"You earned 3 frequent renter points"
	}
})
