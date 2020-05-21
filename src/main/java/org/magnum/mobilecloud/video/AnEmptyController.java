/*
 *
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.magnum.mobilecloud.video;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class AnEmptyController {

    /**
     * You will need to create one or more Spring controllers to fulfill the
     * requirements of the assignment. If you use this file, please rename it
     * to something other than "AnEmptyController"
     * <p>
     * <p>
     * ________  ________  ________  ________          ___       ___  ___  ________  ___  __
     * |\   ____\|\   __  \|\   __  \|\   ___ \        |\  \     |\  \|\  \|\   ____\|\  \|\  \
     * \ \  \___|\ \  \|\  \ \  \|\  \ \  \_|\ \       \ \  \    \ \  \\\  \ \  \___|\ \  \/  /|_
     * \ \  \  __\ \  \\\  \ \  \\\  \ \  \ \\ \       \ \  \    \ \  \\\  \ \  \    \ \   ___  \
     * \ \  \|\  \ \  \\\  \ \  \\\  \ \  \_\\ \       \ \  \____\ \  \\\  \ \  \____\ \  \\ \  \
     * \ \_______\ \_______\ \_______\ \_______\       \ \_______\ \_______\ \_______\ \__\\ \__\
     * \|_______|\|_______|\|_______|\|_______|        \|_______|\|_______|\|_______|\|__| \|__|
     */

    @Autowired
    private VideoRepository videoRepository;

    @RequestMapping(value = "/video", method = RequestMethod.GET)
    public @ResponseBody
    List<Video> goodLuck() {

        System.out.println("good luck");
        List<Video> videos = videoRepository.findAll();

        return videos;
    }


    @RequestMapping("/some/path/{id}")
    @ResponseBody
    public String doSomething(@PathVariable("id") String id, Principal p) {

        System.out.println("doSomething");
        String username = p.getName();
        // Maybe you want to add this users name to
        // the list of people who like a video

        return username;
    }

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    @ResponseBody
    public Video add(@RequestBody Video video) {

        System.out.println("add");
        Video savedVideo = videoRepository.save(video);

        return savedVideo;
    }

    @RequestMapping("/video/{id}")
    @ResponseBody
    public ResponseEntity<Video> getById(@PathVariable("id") long id) {

        System.out.println("getById" + id);

        Video video = videoRepository.findOne(id);

        return video == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(video, HttpStatus.OK);
    }


    @RequestMapping(value = "/video/{id}/like", method = RequestMethod.POST)
    public ResponseEntity like(@PathVariable long id, Principal principal) {

        ResponseEntity responseEntity = null;

        System.out.println("like" + id);
        Video video = videoRepository.findOne(id);
        if (video == null) {
            responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            long likes = video.getLikes();
            Set<String> likedBy = video.getLikedBy();
            if (likedBy.contains(principal.getName())) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
            video.setLikes(likes + 1);
            likedBy.add(principal.getName());
            video.setLikedBy(likedBy);
            videoRepository.save(video);
            responseEntity = new ResponseEntity(HttpStatus.OK);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/video/{id}/unlike", method = RequestMethod.POST)
    public ResponseEntity dislike(@PathVariable long id) {

        ResponseEntity responseEntity = null;

        Video video = videoRepository.findOne(id);
        if (video == null) {
            responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            video.setLikes(video.getLikes() - 1);

            videoRepository.save(video);
            responseEntity = new ResponseEntity(HttpStatus.OK);
        }

        return responseEntity;
    }

    @RequestMapping("/video/search/findByName")
    @ResponseBody
    public List<Video> byName(@RequestParam String title) {

        System.out.println("by name" + title);

        List<Video> videos = videoRepository.findByName(title);

        return videos;
    }

    @RequestMapping("/video/search/findByDurationLessThan")
    @ResponseBody
    public List<Video> byDuration(@RequestParam long duration) {

        System.out.println("by duration" + duration);
        List<Video> videos = videoRepository.findByDurationLessThan(duration);

        return videos;
    }
}
