node_to_insert->next = current_node->next;

    current_node->next = node_to_insert;
    current_node->previous = node_to_insert;