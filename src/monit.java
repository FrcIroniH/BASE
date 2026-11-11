
#include <iostream>
#include <cstdlib>
#include <cstring>
using namespace std;

string chat;

struct student {
    long long id;
    char name[100];
    int age;
};

student** students = nullptr;
int studentcount = 0;

void addkid() {
    student** temp = (student**)realloc(
        students, (studentcount + 1) * sizeof(student*)
    );

    students = temp;

    students[studentcount] = (student*)malloc(sizeof(student));


    cout << "Enter student ID: ";
    cin >> students[studentcount]->id;

    cout << "Enter student name: ";
    cin >> students[studentcount]->name;

    cout << "Enter student age: ";
    cin >> students[studentcount]->age;

    studentcount++;

    cout << "Student added " << endl;
}

void searchkid(long long id) {
    for (int i = 0; i < studentcount; i++) {
        if (students[i]->id == id) {
            cout << "Student name: " << students[i]->name << endl;
            cout << "Student age: " << students[i]->age << endl;
            return;
        }
    }
    cout << "Student not found" << endl;
}

void deletekid(long long id) {
    int i;

    for (i = 0; i < studentcount; i++) {
        if (students[i]->id == id) {
            break;
        }
    }

    if (i == studentcount) {
        cout << "Student not found" << endl;
        return;
    }

    free(students[i]);

    for (int j = i; j < studentcount - 1; j++) {
        students[j] = students[j + 1];
    }

    studentcount--;

    if (studentcount == 0) {
        free(students);
        students = nullptr;
    }
    else {
        student** temp = (student**)realloc(
            students, studentcount * sizeof(student*)
        );

        students = temp;
    }
    cout << "Student deleted" << endl;
}

int main() {
    while (true) {
        cout << "1. Add student" << endl;
        cout << "2. Delete student" << endl;
        cout << "3. Search student;" << endl;
        cout << "4. Exit" << endl;

        cin >> chat;

        if (chat == "1") {
            addkid();
        }
        else if (chat == "2") {
            long long id;
            cout << "Enter ID: ";
            cin >> id;
            deletekid(id);
        }
        else if (chat == "3") {
            long long id;
            cout << "Enter ID: ";
            cin >> id;
            searchkid(id);
        }
        else if (chat == "4") {
            break;
        }
        else {
            cout << "Invalid choice" << endl;
        }
    }
}
