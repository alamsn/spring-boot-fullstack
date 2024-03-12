import {
    Button,
    Drawer, DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, useColorModeValue,
    useDisclosure
} from "@chakra-ui/react";
import CustomerForm from "./CustomerForm.jsx";
import React from "react";

const CustomerUpdateDrawerForm = ({ id, name, email, age, gender, fetchCustomers }) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return <>
        <Button
            w={'40%'} mt={8}
            bg={useColorModeValue('#151f21', 'gray.900')}
            color={'white'} rounded={'md'}
            _hover={{
                transform: 'translateY(-2px)',
                boxShadow: 'lg',}}
            onClick={onOpen}
        >
            Update
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={'lg'}>
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>Update Customer</DrawerHeader>

                <DrawerBody>
                    <CustomerForm
                        id={id}
                        name={name}
                        email={email}
                        age={age}
                        gender={gender}
                        fetchCustomers={fetchCustomers}/>
                </DrawerBody>

                <DrawerFooter>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>
}

export default CustomerUpdateDrawerForm