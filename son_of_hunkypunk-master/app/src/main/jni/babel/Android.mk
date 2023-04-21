#	Copyright © 2009 Rafał Rzepecki <divided.mind@gmail.com>
#
#	This file is part of Hunky Punk.
#
#    Hunky Punk is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    Hunky Punk is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with Hunky Punk.  If not, see <http://www.gnu.org/licenses/>.

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := babel
LOCAL_SRC_FILES := babel.cpp zcode.c tads.c tads2.c tads3.c md5.c blorb.c \
glulx.c babel_handler.c register.c misc.c ifiction.c register_ifiction.c 
LOCAL_CPPFLAGS	:= -W -Wall

include $(BUILD_SHARED_LIBRARY)
