#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
	Api unit test Script Version 0.2.1
	Last Edit : Feb 26, 2020
	
	CHANGELOG
	---------
	0.2.1
	 - Add Python 3/2 support
	0.2.0
	 - Add files support
	0.0.1
	 - Creation
	
'''
from distutils import command

import argparse
import subprocess
import signal	
import os,sys
import re
import platform
import requests
import json,base64
import time,datetime
from random import randint
import uuid

import threading
import hashlib
import base64
import binascii


from requests import Request, Session


log_file = None

#---
#--- Base elements
#---
class APIInteractiveExplorer():
	API_BASE_URL = None
	SSL_VERIFY = True
	
	def custom_headers(self):
		return None
		
	def __get_header(self,headers):
		custom_headers = self.custom_headers()
		if custom_headers is not None:
			headers.update(custom_headers)
		return headers
		
	def _execute_request(self, prepared_request):
		_session = Session()
		prepared_request.headers.update({'User-Agent': 'API Test'})	
		prepared_request.headers = self.__get_header(prepared_request.headers)
		response = _session.send(prepared_request.prepare(), verify=self.SSL_VERIFY)
		return response

	def get_request(self, end_point, headers={}, params={},name=None,silent=False):
		prepared_request = Request('GET', self.API_BASE_URL + end_point, params=params,headers=headers)
		return self.log_transaction(lambda: self._execute_request(prepared_request),name,silent=silent)
	
	def put_request(self, end_point, headers={}, data=None,json_data=None,files=None, params={},name=None,silent=False):
		request_data = None
		if data is not None:
			request_data = data
		elif json_data is not None:
			request_data = json.dumps(json_data)
			headers["Content-Type"]="application/json"
		prepared_request = Request('PUT', self.API_BASE_URL + end_point, params=params, data=request_data,files=files,headers=headers)
		return self.log_transaction(lambda:self._execute_request(prepared_request),name,silent=silent)
	
	def post_request(self, end_point, headers={}, params={}, data=None,json_data=None,files=None,name=None,silent=False):
		request_data = None
		if data is not None:
			request_data = data
		elif json_data is not None:
			request_data = json.dumps(json_data)
			headers["Content-Type"]="application/json"
		prepared_request = Request('POST', self.API_BASE_URL + end_point, params=params, data=request_data,files=files,headers=headers)
		return self.log_transaction(lambda: self._execute_request(prepared_request),name,silent=silent)

	def patch_request(self, end_point, headers={}, params={}, data=None,json_data=None,name=None,silent=False):
		request_data = None
		if data is not None:
			request_data = data
		elif json_data is not None:
			request_data = json.dumps(json_data)
			headers["Content-Type"]="application/json"
		prepared_request = Request('PATCH', self.API_BASE_URL + end_point, params=params, data=request_data,headers=headers)
		return self.log_transaction(lambda: self._execute_request(prepared_request),name,silent=silent)
	
	# Custom logger
	def __log(self,log_string):
		if log_file is not None:
			log_file.write(log_string.encode("utf-8"))
		print(log_string)
	
	def log_transaction(self,request,name=None,silent = False):
		start = datetime.datetime.now()
		response = request()
		if response is None:
			return
		if silent is False:
			end = datetime.datetime.now()
			
			duration = end-start
			try:
				display_request = (u"METHOD : {}".format(str(response.request.method)) + "\n"  +
									u"URL : {}".format(response.request.url) + "\n"  +
									u"HEADERS : {}".format(response.request.headers) + "\n"  +
									u"BODY : {}".format(response.request.body)
									)
			except:
				display_request = (u"METHOD : {}".format(str(response.request.method)) + "\n"  +
									u"URL : {}".format(response.request.url) + "\n"  +
									u"HEADERS : {}".format(response.request.headers)
									)
				
			display_response = (u"URL : {}".format(response.url) + "\n"  +
							u"STATUS CODE : {}".format(str(response.status_code)) + "\n" +
							u"HEADER : {}".format(response.headers) + "\n" +
							u"DURATION : {}".format(duration) + "\n"  +
							u"RAW RESPONSE : {}".format(response.text) 
							)
							
			
			
			if name is not None:
				self.__log(u'\n===============================\n{0}\n-----REQUEST-----\n{1}'.format(name,display_request))
			self.__log(u'\n===============================\n-----REQUEST-----\n{0}'.format(display_request))
			self.__log(u"\n----RESPONSE-----\n{0}\n===============================\n\n".format(display_response) )
		else:
			self.__log(u'\n{0} {1}'.format(str(response.request.method),response.request.url))
			
		return response
	
	
	def __request_action_choice(self):
		#current_module = sys.modules[__name__]
		list_actions = []
		exit_action = None
		for item in dir(self):
			if item.startswith("action_"):
				if item=="action_exit":
					exit_action = item
					continue
				list_actions.append(item)
		list_actions.append(exit_action)
		menu_text = "\nChoose the action you wish to execute:"
		for i in range(0,len(list_actions)):
			action_name = list_actions[i].replace("action_","").replace("_"," ").title()
			menu_text = menu_text+"\n{0}. {1}".format(i+1,action_name)
		
		print(menu_text)
		if sys.version_info[0] == 3:
			answer = input("Select one:")
		elif sys.version_info[0] == 2:
			answer=raw_input("Select one:")
		try:
			int_answer = int(answer)
		except:
			print("----"*5)
			print("Please provide a valid value")
			return self.__request_action_choice()
		
		if int_answer>len(list_actions):
			print("----"*5)
			print("Please provide a value in the valid range 1 to {0}".format(len(list_actions)))
			return self.__request_action_choice()
		
		return list_actions[int_answer-1]
		
	
	def __prompt_confirmation(self,prompt_question,default=None):
		question_append = ' (Yes/No): '
		if default is not None:
			if default is True:
				question_append = ' (Yes/No) [Yes]: '
			elif default is False:
				question_append = ' (Yes/No) [No]: '
		if sys.version_info[0] == 3:
			reply = str(input(prompt_question+question_append)).lower().strip()
		elif sys.version_info[0] == 2:
			reply = str(raw_input(prompt_question+question_append)).lower().strip()
				
		if len(reply)==0 and default is not None:
			return default
		if reply[0] == 'y' or reply[0] == 'yes' :
			return True
		if reply[0] == 'n' or reply[0] == 'no' :
			return False
		else:
			return self.__prompt_confirmation("Uhhhh... please enter a valid value")

	def action_exit(self):
		pass	
	
	def main(self):
		action = None
		log_file=None
		log_into_file = self.__prompt_confirmation("Log action in file",default=False)
		if log_into_file == True:
			log_file = open(time.strftime("%d-%m-%Y_%H_%m_%s") + ".txt", "w+")
			
		
		while action !="action_exit":
			action = self.__request_action_choice()
			if action == "action_exit":
				continue
			action_name = action.replace("_", " ").replace("action", "").title()
			#globals()[action]()
			func = getattr(self, action)
			func()
		#locals()[action]()
		if log_file is not None:
			log_file.close()

#---
#--- Project specific elements
#---

class KungryAPI(APIInteractiveExplorer):
	import time
	
	API_BASE_URL = "https://kungry.ca/api/v1" #Must Be defined
	SSL_VERIFY = True
	
	CLIENT_ID="STO4WED2NTDDxjLs8ODios5M15HwsrRlydsMa1t0"
	CLIENT_SECRET="YOVWGpjSnHd5AYDxGBR2CIB09ZYM1OPJGnH3ijkKwrUMVvwLprUmLf6fxku06ClUKTAEl5AeZN36V9QYBYvTtrLMrtUtXVuXOGWleQGYyApC2a469l36TdlXFqAG1tpK"
	USERNAME = "test{}@test.com".format(int(time.time()))
	PASSWORD = "test1234"
	
	ACCESS_TOKEN = None
	REFRESH_TOKEN = None
	CREATED_REVIEW_ID = None
	
	def custom_headers(self):
		headers = {}
		return headers

	#---
	#--- Account Management
	#---

	def action_create_account(self):
		params = {'first_name':"Python", 
				'last_name':'Script',
				"email":self.USERNAME,
				"client_id":self.CLIENT_ID,
				"client_secret":self.CLIENT_SECRET,
				"password":self.PASSWORD}
		create_account_request = self.post_request('/account', json_data=params)
		json_dict = create_account_request.json()
		if "content" in json_dict and json_dict["content"] is not None:
			if "access_token" in json_dict["content"]:
				self.ACCESS_TOKEN =  json_dict["content"]["access_token"]
			if "refresh_token" in json_dict["content"]:
				self.REFRESH_TOKEN =  json_dict["content"]["refresh_token"]
		return create_account_request
	
	def action_login(self):
		params = {"email":self.USERNAME,
				"client_id":self.CLIENT_ID,
				"client_secret":self.CLIENT_SECRET,
				"password":self.PASSWORD}
		login_request =  self.post_request('/account/login', json_data=params)
		
		json_dict = login_request.json()
		if "content" in json_dict and json_dict["content"] is not None:
			if "access_token" in json_dict["content"]:
				self.ACCESS_TOKEN =  json_dict["content"]["access_token"]
			if "refresh_token" in json_dict["content"]:
				self.REFRESH_TOKEN =  json_dict["content"]["refresh_token"]
				
		return login_request
		

	def action_account_info(self):
		headers = {"Authorization":"Bearer {}".format(self.ACCESS_TOKEN)}
		return self.get_request('/account/me', headers=headers)	

	def action_refresh_token(self):
		params = {"client_id":self.CLIENT_ID,
				"client_secret":self.CLIENT_SECRET,
				"refresh_token":self.REFRESH_TOKEN}
		refresh_request =  self.post_request('/account/refresh_token', json_data=params)
		
		json_dict = refresh_request.json()
		if "content" in json_dict and json_dict["content"] is not None:
			if "access_token" in json_dict["content"]:
				self.ACCESS_TOKEN =  json_dict["content"]["access_token"]
			if "refresh_token" in json_dict["content"]:
				self.REFRESH_TOKEN =  json_dict["content"]["refresh_token"]
				
		return refresh_request

	#---
	#--- Restaurant Actions
	#---
	
	def action_restaurants_fr(self):
		headers = {"Accept-Language":"fr"}
		return self.get_request('/restaurant', headers=headers)	

	def action_restaurants_en(self):
		headers = {"Accept-Language":"en"}
		return self.get_request('/restaurant', headers=headers)	


	def action_search_restaurant_geolocation(self):
		params = {"latitude":46.7809757,
				"longitude":-71.2700677,
				 "radius":6}
		return self.get_request('/restaurant/search', params=params)	

	def action_search_restaurant_text(self):
		params = {"text":"affaire"}
		return self.get_request('/restaurant/search', params=params)	

	def action_search_restaurant_combo(self):
		params = {"text":"affaire",
				"latitude":46.7809757,
				"longitude":-71.2700677,
				 "radius":5}
		return self.get_request('/restaurant/search', params=params)	


	def action_restaurant_details(self):
		params = {"latitude":46.7809757,
				"longitude":-71.2700677}
		restaurant_id = 3
		return self.get_request('/restaurant/{}'.format(restaurant_id), params=params)

	def action_all_restaurant_reviews(self,pagination=1):
		restaurant_id = 3
		#first request is not whiled
		if pagination ==0:
			return None
		else:
			params = {"page":pagination}
			restaurant_request = self.get_request('/restaurant/{}/reviews'.format(restaurant_id),params=params)
			json_dict = restaurant_request.json()
			if "content" in json_dict:
				if "next" in json_dict["content"]:
					return_pagination = 0
					if json_dict["content"]["next"] is not None:
						return_pagination =json_dict["content"]["next"]
						return self.action_all_restaurant_reviews(return_pagination)
	
	#---
	#--- Review Actions
	#---
	
	def action_create_review(self):
		headers = {"Authorization":"Bearer {}".format(self.ACCESS_TOKEN)}
		params = {"restaurant_id":3,
				  "stars":3,
				  "comment":"Meilleur resto en ville!"}
		review_create = self.post_request('/review', headers=headers,json_data=params)
		json_dict = review_create.json()
		if "content" in json_dict and json_dict["content"] is not None:
			if "id" in json_dict["content"]:
				self.CREATED_REVIEW_ID = json_dict["content"]["id"]
		return review_create	

	def action_my_reviews(self):
		headers = {"Authorization":"Bearer {}".format(self.ACCESS_TOKEN)}
		return self.get_request('/review/mine', headers=headers)


	def action_add_review_image(self):
		self.action_login()
		self.action_create_review()
		
		headers = {"Authorization":"Bearer {}".format(self.ACCESS_TOKEN)}
		files = {'image': open('review_image.jpg', 'rb')}
		if self.CREATED_REVIEW_ID is None:
			print("Cannot execute this request, we don't have a review_id")
		
		return self.post_request('/review/{}/image'.format(self.CREATED_REVIEW_ID), headers=headers,files=files)

#--- Setup function
#---

if __name__ == "__main__":
	KungryAPI().main()

