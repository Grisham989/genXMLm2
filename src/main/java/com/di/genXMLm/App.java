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
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class App
{
    public static void main(String[] args) throws JAXBException, DatatypeConfigurationException, FileNotFoundException
    {
    	Logger logger = Logger.getLogger("logger");	
    	logger.setLevel(Level.INFO);
    	
    	System.out.println("Wprowadz liczbe autorow: "); 	
    	Scanner read = new Scanner(System.in);
    	int liczba_autorow = read.nextInt();
    	
    	HashMap<String, Integer> counts= new HashMap<String, Integer>();
       	//SETUP//
     	counts.put("authors", liczba_autorow);
    	counts.put("customers", 5*liczba_autorow);
    	counts.put("books", 10*liczba_autorow);
    	counts.put("order", 25*liczba_autorow);
    	//SETUP - end//
    	
        ObjectFactory objectFactory = new ObjectFactory();
        
        AuthorsType authorsType = objectFactory.createAuthorsType();
        for (int it_authors=0; it_authors<counts.get("authors"); it_authors++)        {
        	if (it_authors % 10000 == 0){
        		StringBuilder sb = new StringBuilder("a: ");
        		sb.append(it_authors).append("/").append(counts.get("authors"));
        		logger.info(sb.toString());
        	}
        	AuthorType authorType = objectFactory.createAuthorType();
        	authorType.setFirstName(randomString(true, 10, true, true));
        	authorType.setLastName(randomString(true, 10, true, true));
        	authorType.setPseudonym(randomString(true, 10, false, true));
        	
        	String code = randomString(false, 15, false, false);
        	StringBuilder sb = new StringBuilder(code);
        	sb.append(it_authors);
        	authorType.setCode(sb.toString());
        	authorsType.getAuthor().add(authorType);
        }
        
        logger.info("Generowanie autorow zakonczone");
        
        CustomersType customersType = objectFactory.createCustomersType();
        for (int it_customers=0; it_customers<counts.get("customers"); it_customers++){
        	if (it_customers % 10000 == 0){
        		StringBuilder sb = new StringBuilder("c: ");
        		sb.append(it_customers).append("/").append(counts.get("customers"));
        		logger.info(sb.toString());;
        	}
			
        	CustomerType customerType = objectFactory.createCustomerType();
        	String code = randomString(false, 15, false, false);
        	StringBuilder sb = new StringBuilder(code);
        	sb.append(it_customers);
        	customerType.setCode(sb.toString());
        	
        	code = randomString(false, 32, false, false);
        	sb = new StringBuilder(code);
        	sb.append(it_customers);
        	customerType.setBarCode(sb.toString());

        	customerType.setFirstName(randomString(true, 10, true, true));
        	customerType.setLastName(randomString(true, 10, true, true));
        	code = randomString(false, 10-(Integer.toString(it_customers).length()), false, false);
        	sb = new StringBuilder(code);
        	sb.append(it_customers);
        	customerType.setPesel(sb.toString());
        	
        	code = randomString(false, 9-(Integer.toString(it_customers).length()), false, false);
        	sb = new StringBuilder(code);
        	sb.append(it_customers);
        	customerType.setPhone(sb.toString());
        	customerType.setAddress(randomString(true, 32, true, true));
        	customersType.getCustomer().add(customerType);
}
        logger.info("Generowanie klientow zakonczone");
        
        BooksType booksType = objectFactory.createBooksType();
        for (int it_books = 0; it_books<counts.get("books"); it_books++)
        {
        	if (it_books % 10000 == 0){
        		StringBuilder sb = new StringBuilder("c: ");
        		sb.append(it_books).append("/").append(counts.get("books"));
        		logger.info(sb.toString());
        	}
        	
        	BookType bookType = objectFactory.createBookType();
        	int authorID = (int)Math.round(Math.random()*(counts.get("authors")-1));
        	bookType.setAuthorCode(authorsType.getAuthor().get(authorID).getCode());
        	
        	String code = randomString(false, 32, false, true);
        	StringBuilder sb = new StringBuilder(code);
        	sb.append(it_books);
        	bookType.setCode(sb.toString());
        	bookType.setDescription(randomString(true, 255, true, true));
        	
        	String isbn = randomString(false, 32, false, false);
        	sb = new StringBuilder(isbn);
        	sb.append(it_books);
        	bookType.setISBN(sb.toString());
        	
        	int price = 10+(int) Math.round(Math.random()*190);
        	bookType.setPrice(Integer.toString(price));
        	
        	bookType.setTitle(randomString(true, 32, true, true));
        	booksType.getBook().add(bookType); 	
        }
        logger.info("Generowanie ksiazek zakonczone");
        
        OrdersType ordersType = objectFactory.createOrdersType();
        for (int it_customers = 0; it_customers<counts.get("customers"); it_customers++)
        {
        	if (it_customers % 10000 == 0){
        		StringBuilder sb = new StringBuilder("o: ");
        		sb.append(it_customers).append("/").append(counts.get("customers"));
        		logger.info(sb.toString());
        	}
        	for (int cust_ord = 0; cust_ord<5; cust_ord++){
	        	OrderType orderType = objectFactory.createOrderType();
	        	
	        	// generowanie losowej daty
	        	XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
	        	Random rand = new Random();
	        	cal.setYear(1950+rand.nextInt(64));
	        	cal.setMonth(rand.nextInt(11)+1);
	        	cal.setDay(rand.nextInt(30)+1);
	        	cal.setHour(rand.nextInt(23)+1);
	        	cal.setMinute(rand.nextInt(59)+1);
	        	cal.setSecond(rand.nextInt(59)+1);
	        	orderType.setOrderDate(cal);
	        	
	        	orderType.setCustomerCode(customersType.getCustomer().get(it_customers).getCode());
	        	
	        	int bookID = (int)Math.round(Math.random()*(counts.get("books")-1));
	        	orderType.setBookCode(booksType.getBook().get(bookID).getCode());
	        	
	        	String status = randomString(true, 32, false, true);
	        	orderType.setStatus(status);
	        	ordersType.getOrder().add(orderType);
        	}
        }
        logger.info("Generowanie zamowien zakonczone");
        
        BookStoreType bookStoreType = objectFactory.createBookStoreType();
        bookStoreType.setAuthors(authorsType);
        bookStoreType.setBooks(booksType);
        bookStoreType.setCustomers(customersType);
        bookStoreType.setOrders(ordersType);
        
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        JAXBElement<BookStoreType> element = objectFactory.createBookStore(bookStoreType);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
              
        FileOutputStream os = new FileOutputStream(new File("wynik.xml"));
        marshaller.marshal(element,os);
    }
	
    // charSet - ustala zestaw znakow (true:cyfry;false:litery)
    // firstLetterBig - true: pierwsza litera jest wielka
    // dynamicLength - true: generowany ciag ma dlugosc 5 do length; false:stala dlugosc length
    static String randomString(boolean charSet, int length, boolean firstLetterBig, boolean dynamicLength){		
    	String chars;
    	if (charSet){
    		chars = "abcdefghijklmnopqrstuvwxyz ";
    	}else{
    		chars = "0123456789";
    	}	
    	Random rand = new Random();
    	if (dynamicLength){ 
    		length = (int) (5+Math.round((Math.random()*(length-5))));
    	}
    	StringBuilder sb = new StringBuilder();
    	for (int i=0; i<length; i++) {
    		sb.append(chars.charAt(rand.nextInt(chars.length())));
    	}	
    	if (firstLetterBig){
    		sb.setCharAt(0, (char)(sb.charAt(0)-32));
    	}
    	return sb.toString();
	}
}
