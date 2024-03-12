import {
    Button,
    Drawer, DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import CustomerForm from "./CustomerForm.jsx";
import React from "react";

const AddIcon = () => "+";
const CustomerCreateDrawerForm = ({ fetchCustomers }) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return <>
        <Button
            colorScheme='teal'
            leftIcon={<AddIcon/>}
            onClick={onOpen}
        >
            Create New Customer
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={'lg'}>
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>Create New Customer</DrawerHeader>

                <DrawerBody>
                    <CustomerForm
                        fetchCustomers={fetchCustomers}/>
                </DrawerBody>

                <DrawerFooter>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>
}

export default CustomerCreateDrawerForm