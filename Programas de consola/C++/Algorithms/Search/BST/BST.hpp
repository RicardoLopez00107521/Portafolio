#ifndef BST_HPP
#define BST_HPP

struct Node {
    int data;
    Node* left;
    Node* riight;
};

class BST {
    private:
        Node* root;

    public:
        void insert_element(int);
        Node* search_element(int);
        Node* get_succesor(int);
        void delete_element(int);
        void print_in_order();
};

#endif