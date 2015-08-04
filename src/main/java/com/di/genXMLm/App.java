package com.di.genXMLm;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
 
public class App
 
{
    public static void main(String[] args) throws JAXBException, DatatypeConfigurationException, FileNotFoundException
    {
    	System.out.println("Wprowadz liczbe autorow: ");
    	Scanner read = new Scanner(System.in); //obiekt do odebrania danych od użytkownika
    	int autorow = read.nextInt();
    	HashMap<String, Integer> counts= new HashMap<String, Integer>();
    	
    	//SETUP//
    	double mnoznik = 1;   // mnoznik 1 = 0,6 MB danych
    	counts.put("authors", autorow);
    	counts.put("customers", 5*autorow);
    	counts.put("books", 10*autorow);
    	counts.put("order", 25*autorow);
    	//SETUP - end//
    	
        ObjectFactory factory = new ObjectFactory();
        
        AuthorsType authors = factory.createAuthorsType();
        for(int i=0; i<counts.get("authors"); i++)
        {
        	if(i % 100 == 0)System.out.println(i+"/"+counts.get("authors"));
        	AuthorType author = factory.createAuthorType();
        	author.setFirstName(randomString(true, 10, true, true));
        	author.setLastName(randomString(true, 10, true, true));
        	author.setPseudonym(randomString(true, 10, false, true));
        	
        	String code = "";
        	int result;
        	do
        	{
        		result = 0;
        		code = randomString(false, 15, false, false);
        		if(authors.getAuthor().size() > 0)
        		{
	         		for(int test = 0; test < authors.getAuthor().size(); test++ )
	        		{
	        			if(code.contentEquals(authors.getAuthor().get(test).getCode()))result = 1;
	        		}
        		}
        	}while(result != 0);
        		
        	author.setCode(code);
        	authors.getAuthor().add(author);
        }
        System.out.println("Generacja autorow zakonczona");
        
        CustomersType customers = factory.createCustomersType();
        for(int i=0; i<counts.get("customers"); i++)
		{
        	if(i % 100 == 0)System.out.println(i+"/"+counts.get("customers"));
			CustomerType customer = factory.createCustomerType();
			
        	// Unique code
        	String code = "";
        	int result;
        	do
        	{
        		result = 0;
        		code = randomString(false, 15, false, false);
        		if(customers.getCustomer().size() > 0)
        		{
	         		for(int test = 0; test < customers.getCustomer().size(); test++ )
	        		{
	        			if(code.contentEquals(customers.getCustomer().get(test).getCode()))result = 1;
	        		}
        		}
        	}while(result != 0);
        	customer.setCode(code);

        	// Unique Barcode
        	do
        	{
        		result = 0;
        		code = randomString(false, 32, false, false);
        		if(customers.getCustomer().size() > 0)
        		{
	         		for(int test = 0; test < customers.getCustomer().size(); test++ )
	        		{
	        			if(code.contentEquals(customers.getCustomer().get(test).getBarCode()))result = 1;
	        		}
        		}
        	}while(result != 0);      	
			customer.setBarCode(code);
			customer.setFirstName(randomString(true, 10, true, true));
			customer.setLastName(randomString(true, 10, true, true));
			
        	// Unique Pesel
        	do
        	{
        		result = 0;
        		code = randomString(false, 10, false, false);
        		if(customers.getCustomer().size() > 0)
        		{
	         		for(int test = 0; test < customers.getCustomer().size(); test++ )
	        		{
	        			if(code.contentEquals(customers.getCustomer().get(test).getPesel()))result = 1;
	        		}
        		}
        	}while(result != 0);
			customer.setPesel(code);
			
        	// Unique Phone
        	do
        	{
        		result = 0;
        		code = randomString(false, 9, false, false);
        		if(customers.getCustomer().size() > 0)
        		{
	         		for(int test = 0; test < customers.getCustomer().size(); test++ )
	        		{
	        			if(code.contentEquals(customers.getCustomer().get(test).getPhone()))result = 1;
	        		}
        		}
        	}while(result != 0);
			customer.setPhone(code);
			customer.setAddress(randomString(true, 32, true, true));
			customers.getCustomer().add(customer);
		}
        System.out.println("Generacja klientow zakonczona");
        
        BooksType books = factory.createBooksType();
        for(int i = 0; i<counts.get("books"); i++)
        {
        	if(i % 100 == 0)System.out.println(i+"/"+counts.get("books"));
        	BookType book = factory.createBookType();
        	int authorID = (int)Math.round(Math.random()*(counts.get("authors")-1));
        	book.setAuthorCode(authors.getAuthor().get(authorID).getCode());
        	
        	// Unique BookCode
        	String code = "";
        	int result;
        	do
        	{
        		result = 0;
        		code = randomString(false, 32, false, true);
        		if(books.getBook().size() > 0)
        		{
	         		for(int test = 0; test < books.getBook().size(); test++ )
	        		{
	        			if(code.contentEquals(books.getBook().get(test).getCode()))result = 1;
	        		}
        		}
        	}while(result != 0);
        	book.setCode(code);
        	
        	book.setDescription(randomString(true, 255, true, true));
        	
        	// Unique ISBN
        	String isbn = "";
        	do
        	{
        		result = 0;
        		isbn = randomString(false, 32, false, false);
        		if(books.getBook().size() > 0)
        		{
	         		for(int test = 0; test < books.getBook().size(); test++ )
	        		{
	        			if(isbn.contentEquals(books.getBook().get(test).getISBN()))result = 1;
	        		}
        		}
        	}while(result != 0);
        	book.setISBN(isbn);
        	
        	
        	int price = 10+(int) Math.round(Math.random()*190);
        	book.setPrice(Integer.toString(price));
        	book.setTitle(randomString(true, 32, true, true));
        	books.getBook().add(book); 	
        }
        System.out.println("Generacja ksiazek zakonczona");
        
        OrdersType orders = factory.createOrdersType();
        for(int i = 0; i<counts.get("customers"); i++)
        {
        	if(i % 100 == 0)System.out.println(i+"/"+counts.get("customers"));
        	for(int cust_ord = 0; cust_ord<5; cust_ord++)
        	{
	        	OrderType order = factory.createOrderType();
	        	order.setBookCode("ex");
	        	
	        	//randomowa data
	        	XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
	        	cal.setYear(1950+(int)Math.round(Math.random()*64));
	        	cal.setMonth((int)Math.round(Math.random()*11)+1);
	        	cal.setDay((int)Math.round(Math.random()*30)+1);
	        	cal.setHour((int)Math.round(Math.random()*23));
	        	cal.setMinute((int)Math.round(Math.random()*59));
	        	cal.setSecond((int)Math.round(Math.random()*59));
	        	order.setOrderDate(cal);
	        	
	        	//random customerCode
	        	order.setCustomerCode(customers.getCustomer().get(i).getCode());
	        	
	        	int bookID = (int)Math.round(Math.random()*(counts.get("books")-1));
	        	order.setBookCode(books.getBook().get(bookID).getCode());
	        	
	        	orders.getOrder().add(order);
        	}
        }
        System.out.println("Generacja zamowien zakonczona");
        

        BookStoreType bst = factory.createBookStoreType();
        bst.setAuthors(authors);
        bst.setBooks(books);
        bst.setCustomers(customers);
        bst.setOrders(orders);
        
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        JAXBElement<BookStoreType> element = factory.createBookStore(bst);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
              
        FileOutputStream os = new FileOutputStream(new File("wynik.xml"));
        marshaller.marshal(element,os);
    }
    


		static String randomString(boolean znaki, int length, boolean firstBig, boolean dynamicLength) {
			String chars = "";
			if(znaki)chars = "abcdefghijklmnopqrstuvwxyz "; else chars = "0123456789";
			
		  Random rand = new Random();
		 if(dynamicLength) length = (int) (5+Math.round((Math.random()*(length-5))));
		  StringBuilder buf = new StringBuilder();
		  for (int i=0; i<length; i++) {
		    buf.append(chars.charAt(rand.nextInt(chars.length())));
		  }
		  if(firstBig)
		  {
			  buf.setCharAt(0, (char)(buf.charAt(0)-32));
		  }
		  return buf.toString();
		}


 
}
