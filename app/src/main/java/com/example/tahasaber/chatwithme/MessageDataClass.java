/**
 * Copyright Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tahasaber.chatwithme;



public class MessageDataClass {


    private String msgBody;
    private String msgPublisher;
    private String msgPublisherId;
    private String photoUrl;

    public MessageDataClass() {
    }

    public MessageDataClass(String msgBody, String msgPublisher, String msgPublisherId, String photoUrl) {
        this.msgBody = msgBody;
        this.msgPublisher = msgPublisher;
        this.msgPublisherId = msgPublisherId;
        this.photoUrl = photoUrl;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public void setMsgPublisher(String msgPublisher) {
        this.msgPublisher = msgPublisher;
    }

    public void setMsgPublisherId(String msgPublisherId) {
        this.msgPublisherId = msgPublisherId;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public String getMsgPublisher() {
        return msgPublisher;
    }

    public String getMsgPublisherId() {
        return msgPublisherId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
