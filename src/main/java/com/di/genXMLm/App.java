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
 
public class App
 
{
    public static void main(String[] args) throws JAXBException, DatatypeConfigurationException, FileNotFoundException
    {
 
    	HashMap<String, Integer> counts= new HashMap<String, Integer>();
    	
    	//SETUP//
    	double mnoznik = 10;
    	counts.put("authors", (int)(10*mnoznik));
    	counts.put("customers", (int)(100*mnoznik));
    	counts.put("books", (int)(1000*mnoznik));
    	counts.put("order", (int)(10000*mnoznik));
    	//SETUP - end//
    	
        ObjectFactory factory = new ObjectFactory();
        
        AuthorsType authors = factory.createAuthorsType();
        for(int i=0; i<counts.get("authors"); i++)
        {
        	AuthorType author = factory.createAuthorType();
        	author.setFirstName(randomString(true, 10, true));
        	author.setLastName(randomString(true, 10, true));
        	author.setPseudonym(randomString(true, 10, false));
        	author.setCode(randomString(false, 15, false));
        	authors.getAuthor().add(author);
        }
        System.out.println("Generacja autorow zakonczona");
        
        CustomersType customers = factory.createCustomersType();
        for(int i=0; i<counts.get("customers"); i++)
		{
			CustomerType customer = factory.createCustomerType();
			customer.setCode(randomString(false, 15, false));
			customer.setBarCode(randomString(false, 32, false));
			customer.setFirstName(randomString(true, 10, true));
			customer.setLastName(randomString(true, 10, true));
			customer.setPesel(randomString(false, 10, false));
			customer.setPhone(randomString(false, 9, false));
			customer.setAddress(randomString(true, 32, true));
			customers.getCustomer().add(customer);
		}
        System.out.println("Generacja klientow zakonczona");
        
        BooksType books = factory.createBooksType();
        for(int i = 0; i<counts.get("books"); i++)
        {
        	BookType book = factory.createBookType();
        	int authorID = (int)Math.round(Math.random()*(counts.get("authors")-1));
        	book.setAuthorCode(authors.getAuthor().get(authorID).getCode());
        	book.setCode(randomString(false, 15, false));
        	book.setDescription(randomString(true, 255, true));
        	book.setISBN(randomString(false, 12, false));
        	int price = (int) Math.round(Math.random()*200);
        	book.setPrice(Integer.toString(price));
        	book.setTitle(randomString(true, 32, true));
        	books.getBook().add(book); 	
        }
        System.out.println("Generacja ksiazek zakonczona");
        
        OrdersType orders = factory.createOrdersType();
        for(int i = 0; i<counts.get("books"); i++)
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
        	int customerID = (int)Math.round(Math.random()*(counts.get("customers")-1));
        	order.setCustomerCode(customers.getCustomer().get(customerID).getCode());
        	
        	int bookID = (int)Math.round(Math.random()*(counts.get("books")-1));
        	order.setBookCode(books.getBook().get(bookID).getCode());
        	
        	orders.getOrder().add(order);
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
    


		static String randomString(boolean znaki, int length, boolean firstBig) {
			String chars = "";
			if(znaki)chars = "abcdefghijklmnopqrstuvwxyz"; else chars = "0123456789";
			
		  Random rand = new Random();
		  length = (int) (5+Math.round((Math.random()*(length-5))));
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
