package com.felixpy.codetrial.wishtodo.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.felixpy.codetrial.wishtodo.domain.Todo;
import com.felixpy.codetrial.wishtodo.domain.Wisher;
import com.felixpy.codetrial.wishtodo.repository.TodoRepository;
import com.felixpy.codetrial.wishtodo.repository.WisherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QueryResolver implements GraphQLQueryResolver {
    private final Logger log = LoggerFactory.getLogger(QueryResolver.class);

    private TodoRepository todoRepository;
    private WisherRepository wisherRepository;

    public QueryResolver(TodoRepository todoRepository, WisherRepository wisherRepository) {
        this.todoRepository = todoRepository;
        this.wisherRepository = wisherRepository;
    }

    public List<Todo> todoList() {
        log.debug("GraphQL request to get all Todos");
        return todoRepository.findAll();
    }

    public Optional<Todo> todo(Long id) {
        log.debug("GraphQL request to get Todo : {}", id);
        return todoRepository.findById(id);
    }

    public List<Wisher> wisherList() {
        log.debug("GraphQL request to get all Wishers");
        return wisherRepository.findAll();
    }

    public Optional<Wisher> wisher(Long id) {
        log.debug("GraphQL request to get Wisher : {}", id);
        return wisherRepository.findById(id);
    }
}
