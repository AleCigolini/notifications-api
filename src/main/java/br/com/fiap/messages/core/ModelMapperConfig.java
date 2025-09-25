package br.com.fiap.messages.core;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.modelmapper.ModelMapper;

@ApplicationScoped
public class ModelMapperConfig {

    @Produces
    @ApplicationScoped
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}