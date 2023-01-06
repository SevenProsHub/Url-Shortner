package com.github.amitsureshchandra.urlshortner.controller

import com.github.amitsureshchandra.urlshortner.dto.RespMsg
import com.github.amitsureshchandra.urlshortner.dto.UrlCreateDto
import com.github.amitsureshchandra.urlshortner.service.UrlService
import com.github.amitsureshchandra.urlshortner.utils.UrlUtils
import org.jetbrains.annotations.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

@RestController
class UrlController(val urlService: UrlService, val utilService: UrlUtils) {

    @PostMapping("/api/v1/routes")
    fun createRoute(@RequestBody dto: UrlCreateDto, httpServletRequest: HttpServletRequest): ResponseEntity<RespMsg>{
        if(dto.url == null || dto.url == ""){
            return ResponseEntity.badRequest().body(RespMsg("invalid url"));
        }
        return ResponseEntity.ok(RespMsg( utilService.getServerPath(httpServletRequest) + "/"+ urlService.saveUrl(dto)));
    }

    @GetMapping("/api/v1/routes")
    fun getRoutes(): ResponseEntity<HashMap<String?, String?>>{
        return ResponseEntity.ok(urlService.urlMap);
    }

    @RequestMapping("/{url}")
    fun redirect(@PathVariable @NotNull url:String?,  httpServletResponse: HttpServletResponse){
        if(!urlService.urlMap.containsKey(url)){
            httpServletResponse.setStatus(404);
        }else{
            httpServletResponse.setHeader("Location", urlService.urlMap[url]);
            httpServletResponse.setStatus(302);
        }
    }
    
    @GetMapping
    fun home(): ResponseEntity<RespMsg>{
        return ResponseEntity.ok(RespMsg("short url service"))
    }
}
