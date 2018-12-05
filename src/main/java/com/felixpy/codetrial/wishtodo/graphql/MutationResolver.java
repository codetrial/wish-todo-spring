package com.felixpy.codetrial.wishtodo.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.felixpy.codetrial.wishtodo.domain.Todo;
import com.felixpy.codetrial.wishtodo.domain.Wisher;
import com.felixpy.codetrial.wishtodo.repository.TodoRepository;
import com.felixpy.codetrial.wishtodo.repository.WisherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MutationResolver implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(MutationResolver.class);

    private TodoRepository todoRepository;
    private WisherRepository wisherRepository;

    public MutationResolver(TodoRepository todoRepository, WisherRepository wisherRepository) {
        this.todoRepository = todoRepository;
        this.wisherRepository = wisherRepository;
    }

    public Todo saveTodo(Todo todo, Wisher wisher) {
        log.debug("GraphQL request to save Todo : {}", todo);
        todo.setWisher(wisher);
        return todoRepository.save(todo);
    }

    public ArrayList<Long> removeTodo(ArrayList<Long> todoIds) {
        log.debug("GraphQL request to delete Todo : {}", todoIds);
        for(Long id : todoIds) {
            todoRepository.deleteById(id);
        }
        return todoIds;
    }

    public Wisher saveWisher(Wisher wisher) {
        log.debug("GraphQL request to save Wisher : {}", wisher);
        return wisherRepository.save(wisher);
    }

    public ArrayList<Long> removeWisher(ArrayList<Long> wisherIds) {
        log.debug("GraphQL request to delete Wisher : {}", wisherIds);
        for(Long id : wisherIds) {
            wisherRepository.deleteById(id);
        }
        return wisherIds;
    }
}
