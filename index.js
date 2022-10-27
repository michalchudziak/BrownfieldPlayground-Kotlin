import React from "react";
import { View, Text, StyleSheet, AppRegistry } from "react-native";

console.log('A')
const MyReactComponent = ({text}) => { 
    console.log('B')
    return (
        <View style={styles.container}>
            <Text style={styles.label}>
            {text}
            </Text>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "#FFFFFF"
    },
    label: {
        fontSize: 20,
    },
});

AppRegistry.registerComponent('MyReactComponent', () => MyReactComponent);