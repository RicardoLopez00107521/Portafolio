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
        Node* create_node(int);

    public:
        BST();
        void insert_element(int);
        Node* search_element(int);
        Node* get_succesor(int);
        void delete_element(int);
        void print_in_order();
};

BST::BST() {
    root = new Node();

    root->data = 0;
    root->left = nullptr;
    root->riight = nullptr;
}

Node* BST::create_node(int data) {
    Node* new_node = new Node();

    new_node->data = data;
    new_node->left = nullptr;
    new_node->riight = nullptr;
}

void BST::insert_element(int data) {
    if (root->left == nullptr && root->riight == nullptr) {
        root->data = data;
    }
}

void BST::print_in_order() {
    cout << root->data;
}

#endif