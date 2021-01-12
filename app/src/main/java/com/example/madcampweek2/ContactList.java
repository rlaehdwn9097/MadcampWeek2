package com.example.madcampweek2;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;

public class ContactList {
    private static ContactList contactListInstance = new ContactList();
    private static ArrayList<Contact> contactList = new ArrayList<Contact>();

    private ContactList() {}

    public static ContactList getInstance(){
        return contactListInstance;
    }

    public synchronized static void add(Contact contact) {
        contactList.add(contact);
    }

    public synchronized static void clear() {
        contactList.clear();
    }

    public static Contact get(int position) {
        if (contactList.size() > 0) {
            return contactList.get(position);
        }
        return null;
    }

    public synchronized static boolean update(int id, Contact contact){
        for (Contact item : contactList) {
            if (item.getId() == id) {
                item.setName(contact.getName());
                item.setNumber(contact.getNumber());
                item.setEmail(contact.getEmail());
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean delete(int id) {
        for (Contact item: contactList) {
            if (item.getId() == id ) {
                contactList.remove(item);
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public synchronized static void sort1() {
        contactList.sort(Comparator.naturalOrder());
    }
}
