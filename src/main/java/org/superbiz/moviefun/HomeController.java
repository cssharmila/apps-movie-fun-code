package org.superbiz.moviefun;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.List;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;

    public HomeController(MoviesBean moviesBean) {
        this.moviesBean = moviesBean;
    }

    @RequestMapping("/")
    public String home(){
        return "index";
    }

    @RequestMapping("/setup")

    public String setup(Model model){
        model.addAttribute("movies",moviesBean.getMovies());
        return "setup";
    }

}
