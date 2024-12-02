// @ts-ignore
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import Create from '../src/auth/Create';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { MemoryRouter } from 'react-router-dom';

// Create hoisted mocks for axios methods
// for future ref : https://stackoverflow.com/questions/76072066/mocking-axios-create-using-vitest
const mocks = vi.hoisted(() => ({
    post: vi.fn(),
    get: vi.fn(),
}));

// Mock the axios module
vi.mock('axios', async (importActual) => {
    const actual = await importActual<typeof import('axios')>();

    return {
        default: {
            ...actual.default,
            create: vi.fn(() => ({
                ...actual.default.create(),
                post: mocks.post,
                get: mocks.get,
            })),
        },
    };
});

describe('Create component', () => {
    beforeEach(() => {
        // Reset the mock before each test
        mocks.post.mockReset();
        mocks.get.mockReset();
    });

    it('should render the form correctly', () => {
        render(
            <MemoryRouter>
                <Create />
            </MemoryRouter>
        );

        expect(screen.getByText('Créer une compte')).not.toBeNull();
        expect(screen.getByLabelText('Prénom')).not.toBeNull();
        expect(screen.getByLabelText('Nom')).not.toBeNull();
        expect(screen.getByLabelText('Email')).not.toBeNull();
        expect(screen.getByLabelText('Role')).not.toBeNull();
    });

    it('should update state values on input change', () => {
        render(
            <MemoryRouter>
                <Create />
            </MemoryRouter>
        );

        const firstNameInput = screen.getByLabelText('Prénom');
        const lastNameInput = screen.getByLabelText('Nom');
        const emailInput = screen.getByLabelText('Email');

        fireEvent.change(firstNameInput, { target: { value: 'John' } });
        fireEvent.change(lastNameInput, { target: { value: 'Doe' } });
        fireEvent.change(emailInput, { target: { value: 'john.doe@example.com' } });

        expect(screen.getByDisplayValue('John')).not.toBeNull();
        expect(screen.getByDisplayValue('Doe')).not.toBeNull();
        expect(screen.getByDisplayValue('john.doe@example.com')).not.toBeNull();
    });

    it('should call post on submit', async () => {
        render(
            <MemoryRouter>
                <Create />
            </MemoryRouter>
        );

        const firstNameInput = screen.getByLabelText('Prénom');
        const lastNameInput = screen.getByLabelText('Nom');
        const emailInput = screen.getByLabelText('Email');
        const submitButton = screen.getAllByRole('button', { name: /Sign Up/i })[0];
        fireEvent.change(firstNameInput, { target: { value: 'John' } });
        fireEvent.change(lastNameInput, { target: { value: 'Doe' } });
        fireEvent.change(emailInput, { target: { value: 'john.doe@example.com' } });

        // Mock the resolved value for the post request
        mocks.post.mockResolvedValueOnce({ data: { success: true } });

        fireEvent.click(submitButton);

        await waitFor(() => {
            expect(mocks.post).toHaveBeenCalledWith('/api/v1/users/create', {
                firstName: 'John',
                lastName: 'Doe',
                email: 'john.doe@example.com',
                role: 'PORTEUR',
            });
        });
    });
});