package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;

public interface ConfigBuscaProduto {
    public String getPath();
    public HttpMethod getMethod();
    public BuscaCadProdutos getSearchParameters();
}
