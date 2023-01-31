/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, { Component } from 'react'
import { View, StyleSheet, Text } from 'react-native'
import { HTPageHeaderView, HTPageContentView, HTPageManager, HTSelectedLabel } from 'react-native-selected-page'

class App extends Component {

	constructor(props) {
		super(props)
		this.pageManager = new HTPageManager([
			{ title: '你好', backgroundColor: 'skyblue' },
			{ title: '世界', backgroundColor: 'coral' },
			{ title: '骑单车', backgroundColor: 'pink' },
			{ title: '晒太阳', backgroundColor: 'turquoise' },
			{ title: '喝热水', backgroundColor: 'salmon' },
		])
	}

	render() {
		let Header = this.pageManager.renderHeaderView
		let Content = this.pageManager.renderContentView
		return (
			<View style={styleList.container}>
				<Header 
    				style={{ height: 50, backgroundColor: 'white', borderBottomColor: '#F5F5F5', borderBottomWidth: 1 }}
					titleFromItem={ item => item.title }
					initScrollIndex={ 0 }
					itemContainerStyle={{ paddingHorizontal: 10, marginLeft: 10 }}
					itemTitleStyle={{ fontSize: 17 }}
					itemTitleNormalStyle={{ color: '#333' }}
					itemTitleSelectedStyle= {{ color: 'orange', fontSize: 20}}
					cursorStyle={{ width: 15, height: 2, borderRadius: 1, backgroundColor: 'orange' }}
    			/>
    			<Content 
    			initScrollIndex={this.props.initScrollIndex}
    			renderItem={({item, index}) => {
    				return (
    					<View style={{ flex: 1, backgroundColor: item.backgroundColor }}>
    					</View>
    				)
    			}} />
			</View>
		)
	}

}

const styleList = StyleSheet.create({
	container: {
		flex: 1,
		paddingTop: 50
	}
})

export default App
