// Copyright 2007-2013 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.jszhan.commons.kern.apiext.collection;

/****
 * 对象与String互转
 * 
 * @author andy.zhou
 *
 * @param <V>
 *            对象的类
 */
public interface IValueEncoder<V> {
	/***
	 * 对象转换成String，方法页面使用
	 * 
	 * @param value
	 * @return
	 */
	String toClient(V value);

	/***
	 * String转换成对应的对象
	 * 
	 * @param clientValue
	 * @return
	 */
	V toValue(String clientValue);
}
