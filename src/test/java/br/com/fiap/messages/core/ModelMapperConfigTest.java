package br.com.fiap.messages.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModelMapperConfigTest {

    @Test
    void shouldCreateModelMapperInstance() {
        ModelMapperConfig config = new ModelMapperConfig();

        ModelMapper modelMapper = config.modelMapper();

        assertNotNull(modelMapper);
    }

    @Test
    void shouldMapBetweenSimpleObjects() {
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        SourceObject source = new SourceObject();
        source.setId(1L);
        source.setName("Test Name");
        source.setDescription("Test Description");

        DestinationObject destination = modelMapper.map(source, DestinationObject.class);

        assertNotNull(destination);
        assertNotNull(destination.getId());
        assertNotNull(destination.getName());
        assertNotNull(destination.getDescription());

        assert(destination.getId().equals(source.getId()));
        assert(destination.getName().equals(source.getName()));
        assert(destination.getDescription().equals(source.getDescription()));
    }

    static class SourceObject {
        private Long id;
        private String name;
        private String description;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    static class DestinationObject {
        private Long id;
        private String name;
        private String description;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
